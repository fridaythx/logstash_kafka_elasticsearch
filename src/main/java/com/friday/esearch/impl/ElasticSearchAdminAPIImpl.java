package com.friday.esearch.impl;

import com.friday.esearch.ElasticSearchAdminAPI;
import com.friday.esearch.ElasticSearchConnector;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchAdminAPIImpl implements ElasticSearchAdminAPI {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchAPIImpl.class);

	public void deleteIndexByName(String indexName) throws Exception {
		ElasticSearchConnector connector = ElasticSearchConnector.getSingletonInstance();
		TransportClient client = connector.getClient();
		DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName).execute().actionGet();
		if (dResponse.isAcknowledged()) {
			LOG.info("Delete index successfuly, ack = ture");
		} else {
			LOG.warn("Failed to Delete index, ack = false");
		}

	}
}