package com.friday.esearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchConnector {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConnector.class);
    private TransportClient client;
    
    public void connect() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();        
        client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public void disconnect() {
        if(client != null){
            client.close();
        }
    }
}