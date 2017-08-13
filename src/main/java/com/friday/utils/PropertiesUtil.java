package com.friday.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties getProperties(String path) throws Exception {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(path);
        try {
            properties.load(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return properties;
    }
}