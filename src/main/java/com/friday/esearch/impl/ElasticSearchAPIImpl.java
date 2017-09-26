package com.friday.esearch.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.entity.DelayValueDTO;
import com.friday.entity.Keyword;
import com.friday.entity.KeywordCount;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchConnector;
import com.friday.utils.DateUtil;

public class ElasticSearchAPIImpl implements ElasticSearchAPI {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchAPIImpl.class);

	public KeywordCount countKeyword(Keyword... keywords) throws Exception {
		KeywordCount keywordCount = new KeywordCount();
		ElasticSearchConnector connector = ElasticSearchConnector.getSingletonInstance();
		TransportClient client = connector.getClient();
		long startTime = System.currentTimeMillis();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("@timestamp");
		for (Keyword keyword : keywords) {
			if ("dateRangeFrom".equals(keyword.getKeyword())) {
				rangeQuery.gt(keyword.getValue());
			} else if ("dateRangeTo".equals(keyword.getKeyword())) {
				rangeQuery.lte(keyword.getValue());
			} else {
				boolQuery.must(QueryBuilders.termQuery(keyword.getKeyword(), keyword.getValue()));
				LOG.info("With keyword : [{}] value : [{}]", keywords[0].getKeyword(), keywords[0].getValue());
			}
		}
		SearchResponse response = client.prepareSearch().setQuery(boolQuery).setPostFilter(rangeQuery)
				.addSort("@timestamp", SortOrder.ASC).setSize(1).get();

		long totalHits = response.getHits().getTotalHits();
		keywordCount.setCount(totalHits);
		if (totalHits > 0) {
			SearchHit searchHit = response.getHits().getHits()[0];
			Map<String, Object> source = searchHit.getSource();
			keywordCount.setStartTime(DateUtil.utc2Date((String) source.get("@timestamp")));
		} else {
			keywordCount.setStartTime(new Date());
		}
		LOG.info("Search hits {}", keywordCount.getCount());
		LOG.info("First start time : {}", keywordCount.getStartTime());
		LOG.info("Time used : {}", System.currentTimeMillis() - startTime);
		return keywordCount;
	}

	public Map<String, List<DelayValueDTO>> queryByCondition(Map<String, Object> con, String... indices)
			throws Exception {
		ElasticSearchConnector connector = ElasticSearchConnector.getSingletonInstance();
		TransportClient client = connector.getClient();
		Map<String, List<DelayValueDTO>> listMap = new HashMap<String, List<DelayValueDTO>>();
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
				String key = (String) source.get("host");
				List<DelayValueDTO> list = listMap.get(key);
				if (list == null) {
					listMap.put(key, list = new ArrayList<DelayValueDTO>());
				}
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
		return listMap;
	}
}