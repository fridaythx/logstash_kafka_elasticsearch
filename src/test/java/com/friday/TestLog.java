package com.friday;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog {
    static Logger log = LoggerFactory.getLogger(TestLog.class);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {            
            log.info("test");
        }
    }
}


