package com.friday.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe pooled HTTP executor
 * 
 * @author Friday
 *
 */
public class MultiThreadHttpClientExecutor implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(MultiThreadHttpClientExecutor.class);

	private static MultiThreadHttpClientExecutor instance;

	private PoolingHttpClientConnectionManager connMgr;

	private CloseableHttpClient httpClient;

	private volatile boolean shutdown;

	private MultiThreadHttpClientExecutor() {
		connMgr = new PoolingHttpClientConnectionManager();
		// Change two props below while invoking http requests cross more than one host.
		// Increase max total connection to 50
		connMgr.setMaxTotal(50);
		// Increase default max connection per route to 50
		connMgr.setDefaultMaxPerRoute(50);

		httpClient = HttpClients.custom().setConnectionManager(connMgr).build();
		// Start self checking thread.
		new Thread(this).start();
		LOG.info("The HTTP connection pooling manger self checking started.");
	}

	public String execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return execute(request, null);
	}

	public String execute(HttpUriRequest request, HttpContext context) throws ClientProtocolException, IOException {
		CloseableHttpResponse response = context != null ? httpClient.execute(request, context)
				: httpClient.execute(request);

		try {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} finally {
			response.close();
		}
	}

	public synchronized static MultiThreadHttpClientExecutor getSingleton() {
		if (instance == null) {
			instance = new MultiThreadHttpClientExecutor();
		}
		return instance;
	}

	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					LOG.info("Evicting expired connections and idle connections.");
					// Close expired connections
					connMgr.closeExpiredConnections();
					// Optionally, close connections
					// that have been idle longer than 30 sec
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			// terminate
			LOG.error("The HTTP connection pooling manger self checking has terminated.", ex);
		}
	}
}
