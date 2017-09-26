package com.friday.thread.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.friday.entity.output.Log;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.MessageDefinition;
import com.friday.utils.CheckUtil;
import com.friday.utils.DateUtil;
import com.friday.utils.HttpUtil;

/**
 * 落库任务
 */
public class DatabaseOpTask extends BasicTask {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseOpTask.class);

	public DatabaseOpTask(TaskSource taskSrc) {
		super(taskSrc);
	}

	public void taskRun() {
		Log log = (Log) taskSrc.getTaskEntity();

		try {
			String res = null;
			String url = appProps.getProperty("log.notify.url");
			LOG.info("Notify url : {}", url);
			CheckUtil.record("DataTransmit");
			JSONObject root = new JSONObject();
			if (log.getfCount() > 1) {
				JSONObject json = (JSONObject) JSON.toJSON(log);
				Date now = log.getfEndTime();
				Date date60mAgo = DateUtil.calc(now, DateUtil.FIELD_HOUR, -1);
				json.put("startTime", date60mAgo);
				json.put("endTime", now);
				root.put("data", json);
				LOG.info("Update log");
				LOG.info("Request body : {}\n", JSON.toJSONStringWithDateFormat(root, "yyyy-MM-dd HH:mm:ss"),
						SerializerFeature.PrettyFormat);
				res = HttpUtil.postJson(url, root);
			} else {
				root.put("data", log);
				LOG.info("Insert log");
				LOG.info("Request body : {}\n", JSON.toJSONStringWithDateFormat(root, "yyyy-MM-dd HH:mm:ss"),
						SerializerFeature.PrettyFormat);
				res = HttpUtil.postJson(url, root);
			}
			CheckUtil.reportTimeElapsed("DataTransmit");
			LOG.info("Response of db insert task : {}", res);
		} catch (Exception e) {
			LOG.error(MessageDefinition.UNEXPECTED_ERROR.appendDesc("While executing the database task."), e);
		}
	}
}