package com.friday.esearch.impl;

import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchConnector;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}