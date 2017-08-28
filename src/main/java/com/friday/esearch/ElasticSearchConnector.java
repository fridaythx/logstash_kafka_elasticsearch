package com.friday.esearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConnector {
	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConnector.class);
	private TransportClient client;

	private static ElasticSearchConnector singleton;

	private ElasticSearchConnector() {
	}

	public static synchronized ElasticSearchConnector getSingletonInstance() throws UnknownHostException {
		if (singleton == null) {
			singleton = new ElasticSearchConnector();
			singleton.connect();
		}
		return singleton;
	}

	public void connect() throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	}

	public TransportClient getClient() {
		return client;
	}

	public void disconnect() {
		if (client != null) {
			client.close();
		}
	}
}