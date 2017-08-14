package com.friday.thread.dispatcher;

import com.friday.thread.TaskSource;
import com.friday.thread.task.AlertTask;
import com.friday.thread.task.DataCleanTask;
import com.friday.thread.task.DatabaseOpTask;
import com.friday.thread.task.PreLogicTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncTaskDispatch implements TaskDispatch {
    private static final Logger LOG = LoggerFactory.getLogger(SyncTaskDispatch.class);
   
    public void dispatchTask(TaskSource taskSrc) throws RuntimeException {
        switch (taskSrc.getTaskType()) {
        case PreLogicTask:
            runPreLogicTask(taskSrc);
            break;
        case AlertTask:
            runAlertTask(taskSrc);
            break;
        case DbOpTask:
            runDbOpTask(taskSrc);
            break;
        case DataCleanTask:
            runDataCleanTask();
            break;
        }
        LOG.info(String.format("Dispatch task successfully, taskType [%s]", taskSrc.getTaskType().toString()));
    }

    public void runPreLogicTask(TaskSource taskSrc) {
        new PreLogicTask(taskSrc).run();
    }

    public void runAlertTask(TaskSource taskSrc) {
        new AlertTask(taskSrc).run();
    }

    public void runDbOpTask(TaskSource taskSrc) {
        new DatabaseOpTask(taskSrc).run();
    }

    public void runDataCleanTask() {
        new DataCleanTask().run();
    }
}