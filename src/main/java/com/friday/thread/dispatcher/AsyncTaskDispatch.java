package com.friday.thread.dispatcher;

import com.friday.thread.TaskSource;
import com.friday.thread.task.AlertTask;
import com.friday.thread.task.DatabaseOpTask;
import com.friday.thread.task.PreLogicTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTaskDispatch implements TaskDispatch {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncTaskDispatch.class);
    private ExecutorService alertExecutorService = Executors.newFixedThreadPool(1);
    private ExecutorService preLogicExecutorService = Executors.newFixedThreadPool(10);
    private ExecutorService dbOpTaskExecutorService = Executors.newFixedThreadPool(10);

    public void addPreLogicTask(TaskSource taskSrc) {
        preLogicExecutorService.execute(new PreLogicTask(taskSrc));
    }

    public void addAlertTask(TaskSource taskSrc) {
        //删冗
        //告警
        alertExecutorService.execute(new AlertTask(taskSrc));
    }

    public void addDbOpTask(TaskSource taskSrc) {
        dbOpTaskExecutorService.execute(new DatabaseOpTask(taskSrc));
    }

    public void dispatchTask(TaskSource taskSrc) throws RuntimeException {
        switch (taskSrc.getTaskType()) {
        case PreLogicTask:
            addPreLogicTask(taskSrc);
            break;
        case AlertTask:
            addAlertTask(taskSrc);
            break;
        case DbOpTask:
            addDbOpTask(taskSrc);
            break;
        }
        LOG.info(String.format("Dispatch task successfully, taskType [%s]", taskSrc.getTaskType().toString()));
    }
}