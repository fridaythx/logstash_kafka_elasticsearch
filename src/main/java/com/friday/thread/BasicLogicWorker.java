package com.friday.thread;

import com.friday.consumer.DelegatingMessageHandle;
import com.friday.consumer.Message;
import com.friday.consumer.MessageConsumer;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicLogicWorker implements Runnable, DelegatingMessageHandle {
    private static final Logger LOG = LoggerFactory.getLogger(BasicLogicWorker.class);
    private boolean running = true;
    private String identifier;
    private Properties appProps;
    private MessageConsumer consumer;
    private TaskDispatcher taskDispatcher = new TaskDispatcher();

    public BasicLogicWorker(Properties appProps, String identifier) {
        this.appProps = appProps;
        this.identifier = identifier;
        consumer = new MessageConsumer(this);
    }

    public void run() {
        try {
            consumer.init(appProps);
            while (running) {
                try {
                    consumer.consume();
                } catch (Exception e) {
                    LOG.error("Unexpected error occurred during consuming records", e);
                    consumer.stop();
                }
            }
        } catch (Exception e) {
            LOG.error("Worker exception.", e);
        }
    }

    public void stop() {
        running = false;
        consumer.stop();
    }

    public void handleMessage(Message message) throws Exception {
        taskDispatcher.dispatchTaskSync(new TaskSource(message, TaskType.PreLogicTask, taskDispatcher));
    }

}