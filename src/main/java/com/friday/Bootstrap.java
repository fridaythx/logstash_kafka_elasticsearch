package com.friday;

import com.friday.thread.BasicLogicWorker;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
    private final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    private static Properties appProps;
    public static void appStart(Properties properties){
        appProps = properties;
        startWorkerThread();
        LOG.info("WorkerThread started...");
        startHttpServerListener();
        LOG.info("HTTPServer listener started...");
        startDataCleanTimer();
        LOG.info("DataCleanTimer started...");
    }

    private static void startWorkerThread(){
        new Thread(new BasicLogicWorker(appProps)).start();;
    }

    private static void startHttpServerListener(){
        
    }

    private static void startDataCleanTimer(){
        
    }
}