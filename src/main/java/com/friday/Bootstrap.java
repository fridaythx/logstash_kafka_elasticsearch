package com.friday;

import com.friday.httpserver.HttpServerHandler;
import com.friday.schedule.MyScheduler;
import com.friday.thread.BasicLogicWorker;
import java.util.Map.Entry;
import java.util.Properties;
import org.eclipse.jetty.server.Server;
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
        startScheduler();
        LOG.info("Scheduler started...");
        startHttpServerListener();
        LOG.info("HTTPServer listener started...");
    }

    private static void startWorkerThread() {
        new Thread(new BasicLogicWorker(appProps, "Worker-1")).start();
    }

    private static void startHttpServerListener() {
        String port = appProps.getProperty("httpserver.port");
        try{
            Server server = new Server(Integer.parseInt(port));
            server.setHandler(new HttpServerHandler());
            server.start();
            server.join();
        }catch(Exception e){
            LOG.error(String.format("Failed to start HttpServer at port {}.",port), e);
        }

    }

    private static void startScheduler() {
        try {
            MyScheduler.getInstance(appProps).registerDataCleanJob().registerAccAvgJob().start();
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