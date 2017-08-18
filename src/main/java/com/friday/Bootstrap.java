package com.friday;

import com.friday.schedule.MyScheduler;
import com.friday.thread.BasicLogicWorker;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
    private final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    private static Properties appProps;

    public static void appStart(Properties properties) {
        appProps = properties;
        printAppProps();
        startWorkerThread();
        LOG.info("WorkerThread started...");
        startHttpServerListener();
        LOG.info("HTTPServer listener started...");
        startScheduler();
        LOG.info("Scheduler started...");
    }

    private static void startWorkerThread() {
        new Thread(new BasicLogicWorker(appProps, "Worker-1")).start();
    }

    private static void startHttpServerListener() {

    }

    private static void startScheduler() {
        try {
            MyScheduler.getInstance(appProps).registerDataCleanJob().start();
        } catch (Exception e) {
            LOG.error("Failed to start scheduler.", e);
        }
    }

    public static void printAppProps() {
        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        for (Entry<Object, Object> kv : appProps.entrySet()) {
            sb.append(kv.getKey() + " = " + kv.getValue() + lineSeparator);

        }
        LOG.info("App props : {}{}", lineSeparator, sb);
    }
}