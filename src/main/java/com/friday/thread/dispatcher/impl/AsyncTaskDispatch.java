package com.friday.thread.dispatcher.impl;

import com.friday.thread.TaskSource;
import com.friday.thread.dispatcher.TaskDispatch;
import com.friday.thread.task.AlertTask;
import com.friday.thread.task.DatabaseOpTask;
import com.friday.thread.task.NotifyDelayValTask;
import com.friday.thread.task.PreLogicTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTaskDispatch implements TaskDispatch {
	private static final Logger LOG = LoggerFactory.getLogger(AsyncTaskDispatch.class);
	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	public void addPreLogicTask(TaskSource taskSrc) {
		executorService.execute(new PreLogicTask(taskSrc));
	}

	public void addAlertTask(TaskSource taskSrc) {
		executorService.execute(new AlertTask(taskSrc));
	}

	public void addDbOpTask(TaskSource taskSrc) {
		executorService.execute(new DatabaseOpTask(taskSrc));
	}
	
	public void addNotifyDelayValTask(TaskSource taskSrc) {
		executorService.execute(new NotifyDelayValTask(taskSrc));
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
		case DelayValueNotifyTask:
			addNotifyDelayValTask(taskSrc);
			break;
		}
		LOG.info(String.format("Dispatched task successfully, taskType [%s]", taskSrc.getTaskType().toString()));
	}
}