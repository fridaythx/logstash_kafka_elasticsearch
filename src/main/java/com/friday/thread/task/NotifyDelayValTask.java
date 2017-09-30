package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.MessageDefinition;
import com.friday.utils.HttpUtil;

/**
 * 延时数据同步任务
 * @author Friday
 *
 */
public class NotifyDelayValTask extends BasicTask {
	private static final Logger LOG = LoggerFactory.getLogger(NotifyDelayValTask.class);

	public NotifyDelayValTask(TaskSource taskSrc) {
		super(taskSrc);
	}

	@Override
	public void taskRun() {
		LOG.info("Notifying remote server with new delayVal...");
		try {
			Object rs = taskSrc.getTaskEntity();
			String url = appProps.getProperty("delayVal.notify.url");
			LOG.info("Request body : {}\n", JSON.toJSONStringWithDateFormat(rs, "yyyy-MM-dd HH:mm:ss"));
			LOG.info("Notify url : {}", url);
			String res = HttpUtil.postJson(url, rs);
			LOG.info("Response : {}", res);
		} catch (Exception e) {
			LOG.error(MessageDefinition.UNEXPECTED_ERROR.appendDesc("While running NotifyDelayValTask."), e);
		}
	}

}