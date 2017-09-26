package com.friday.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.http.HttpHeader;

import com.alibaba.fastjson.JSON;

public class HttpUtil {
	public static final String HTTP_HEADER_USER_AGENT = HttpHeader.USER_AGENT.asString();
	
	public static String postForm(String url, Map<String, String> params) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			for (Entry<String, String> entry : params.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			HttpEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			httpPost.setEntity(entity);
		}
		MultiThreadHttpClientExecutor executor = MultiThreadHttpClientExecutor.getSingleton();
		return executor.execute(httpPost);
	}

	public static String postJson(String url, Object json) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		String jsonStr = null;
		if (!(json instanceof String)) {
			jsonStr = JSON.toJSONStringWithDateFormat(json, "yyyy-MM-dd HH:mm:ss");
		} else {
			jsonStr = (String) json;
		}
		StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
		httpPost.setEntity(entity);
		MultiThreadHttpClientExecutor executor = MultiThreadHttpClientExecutor.getSingleton();
		return executor.execute(httpPost);
	}

	public static String get(String url,Header...headers) throws Exception {
		MultiThreadHttpClientExecutor executor = MultiThreadHttpClientExecutor.getSingleton();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeaders(headers);
		return executor.execute(httpGet);
	}
}
