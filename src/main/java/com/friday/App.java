package com.friday;

import com.friday.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            Bootstrap.appStart(PropertiesUtil.getProperties("app.properties"));
            LOG.info("App started...");
        } catch (Exception e) {
            LOG.error("Failed to start app.", e);
        }
    }
}
