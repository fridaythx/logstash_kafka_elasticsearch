package com.friday.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	private static Map<String, Properties> store = new HashMap<String, Properties>();

	public static synchronized Properties getProperties(String path) {
		if (store.containsKey(path)) {
			return store.get(path);
		}
		return reloadProperties(path);

	}

	public static synchronized Properties reloadProperties(String path) {
		Properties loadProperties = loadProperties(path);
		store.put(path, loadProperties);
		return loadProperties;
	}

	private static Properties loadProperties(String path) {
		Properties properties = new Properties();
		InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
		try {
			properties.load(input);
		} catch (Exception e) {
			throw new RuntimeException("Unable to load properties : " + path, e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return properties;
	}
}