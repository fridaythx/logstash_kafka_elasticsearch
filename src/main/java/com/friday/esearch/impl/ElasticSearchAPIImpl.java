package com.friday.esearch.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.entity.dto.DelayValueDTO;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchConnector;

public class ElasticSearchAPIImpl implements ElasticSearchAPI {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchAPIImpl.class);

	public long countKeyword(String key, String value) throws Exception {
		ElasticSearchConnector connector = ElasticSearchConnector.getSingletonInstance();
		TransportClient client = connector.getClient();
		long startTime = System.currentTimeMillis();
		LOG.info("With keyword : [{}] value : [{}]", key, value);
		SearchResponse response = client.prepareSearch().setQuery(QueryBuilders.termQuery(key, value)).get();
		LOG.info("Search hits {}", response.getHits().getTotalHits());
		LOG.info("Time used : {}", System.currentTimeMillis() - startTime);
		return response.getHits().getTotalHits();
	}

	public List<DelayValueDTO> queryByCondition(Map<String, Object> con, String... indices) throws Exception {
		ElasticSearchConnector connector = ElasticSearchConnector.getSingletonInstance();
		TransportClient client = connector.getClient();
		List<DelayValueDTO> list = new ArrayList<DelayValueDTO>();
		long startTime = System.currentTimeMillis();
		SearchResponse scrollResp = client.prepareSearch(indices).setTypes((String) con.get("searchType"))
				.setScroll(new TimeValue(60000))
				.setQuery(
						QueryBuilders.rangeQuery("@timestamp").gt(con.get("dateRangeFrom")).lte(con.get("dateRangeTo")))
				.setSize(1000).get();

		// Scroll until no hits are returned
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				Map<String, Object> source = hit.getSource();
				// Handle the hit...
				DelayValueDTO delayValueDTO = new DelayValueDTO();
				delayValueDTO.setToVsDelayVal((String) source.get("toVsDelayVal"));
				delayValueDTO.setToServerDelayVal((String) source.get("toServerDelayVal"));
				delayValueDTO.setVsAddress((String) source.get("vsAddress"));
				delayValueDTO.setServerAddress((String) source.get("serverAddress"));
				list.add(delayValueDTO);
			}

			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (scrollResp.getHits().getHits().length != 0); 
		LOG.info("Time used : {}", System.currentTimeMillis() - startTime);
		return list;
	}
}