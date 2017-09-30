package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.thread.TaskSource;
import com.friday.thread.constant.MessageDefinition;

/**
 * 告警任务
 * @author Friday
 *
 */
public class AlertTask extends BasicTask {
	private static final Logger LOG = LoggerFactory.getLogger(AlertTask.class);

	public AlertTask(TaskSource taskSrc) {
		super(taskSrc);
	}

	public void taskRun() {
		LOG.info(MessageDefinition.NO_IMPLEMENTATION.appendDesc("No need to alert now."));
	}
}