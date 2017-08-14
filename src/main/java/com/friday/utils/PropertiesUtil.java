package com.friday.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties getProperties(String path) throws Exception {
        Properties properties = new Properties();
        InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
        try {
            properties.load(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return properties;
    }
}