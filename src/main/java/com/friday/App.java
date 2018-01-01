package com.friday;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.utils.PropertiesUtil;

/**
 * App 入口
 *
 */
public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static final String APP_PROPS_PATH = "app.properties";

	public static final String APP_VERSION = "version";

	public static void main(String[] args) {
		try {
			Properties properties = PropertiesUtil.getProperties(APP_PROPS_PATH);

			LOG.info("App version {}", properties.get(APP_VERSION));

			LOG.info("App starting...");

			Bootstrap.appStart(properties);

			LOG.info("App started...");
		} catch (Exception e) {
			LOG.error("Failed to start app.", e);
		}
	}
}
