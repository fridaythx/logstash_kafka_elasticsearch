package com.friday.thread.task;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.App;
import com.friday.thread.TaskSource;
import com.friday.utils.PropertiesUtil;

/**
 * 任务基类
 * @author Friday
 *
 */
public abstract class BasicTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(BasicTask.class);
	
    private long startTime;
    private long endTime;
    protected TaskSource taskSrc;
    protected Properties appProps;

    public BasicTask(TaskSource taskSrc) {
        this.taskSrc = taskSrc;
        this.appProps = PropertiesUtil.getProperties(App.APP_PROPS_PATH);
    }

    public void run() {
        onStart();
        taskRun();
        onEnd();
        LOG.info(String.format("[%s] Task ended, %d ms used.", taskSrc.getTaskType(), endTime - startTime));
    }

    private void onStart() {
        this.startTime = System.currentTimeMillis();
        beforeTaskRun();
    }

    private void onEnd() {
        this.endTime = System.currentTimeMillis();
        afterTaskRun();
    }

    public void beforeTaskRun() {
        // to be implemented by derived classes...
    }

    public void afterTaskRun() {
        // to be implemented by derived classes...
    }

    public abstract void taskRun();

}