package com.friday.esearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.App;
import com.friday.utils.PropertiesUtil;

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
			LOG.info("Elastic connection has been established.");
		}
		return singleton;
	}

	public void connect() throws UnknownHostException {
		Properties appProps = PropertiesUtil.getProperties(App.APP_PROPS_PATH);
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		String hostKey = "elasticserver.dev.host", portKey = "elasticserver.dev.port";
		if ("production".equals(appProps.getProperty("env"))) {
			hostKey = "elasticserver.prd.host";
			portKey = "elasticserver.prd.port";
		}
		String host = appProps.getProperty(hostKey);
		String port = appProps.getProperty(portKey);
		LOG.info("Connecting to Elasticsearch server [{}:{}] ...", host, port);
		client = new PreBuiltTransportClient(settings).addTransportAddress(
				new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port)));
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