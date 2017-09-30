package com.friday;

import com.friday.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * App 入口
 *
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    
    public static final String APP_PROPS_PATH = "app.properties";

    public static void main(String[] args) {
        try {
            LOG.info("App starting...");
            Bootstrap.appStart(PropertiesUtil.getProperties(APP_PROPS_PATH));
            LOG.info("App started...");
        } catch (Exception e) {
            LOG.error("Failed to start app.", e);
        }
    }
}
