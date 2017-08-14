package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.thread.TaskSource;

public class AlertTask extends BasicTask {
    private static final Logger LOG = LoggerFactory.getLogger(AlertTask.class);

    public AlertTask(TaskSource taskSrc){
        super(taskSrc);
    }

    public void taskRun() {
        LOG.info("Invokeing remote api to alert...");
    }
}