package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.friday.thread.TaskSource;

/**
 * 落库任务
 */
public class DatabaseOpTask extends BasicTask {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseOpTask.class);

    public DatabaseOpTask(TaskSource taskSrc) {
        super(taskSrc);
    }

    public void taskRun() {
        JSONObject jsonObj = (JSONObject) taskSrc.getTaskEntity();
        long count = jsonObj.getLong("count");
        if (count == 0) {
            LOG.info(
                    "Insert into db with fType = normal,fCount = {}, fStartTime = {}, fLastTime = {}, fEndTime = {}, fStatus = {}, fContent = {}",
                    count, jsonObj.getDate("timestamp"), jsonObj.getDate("timestamp"), jsonObj.getDate("timestamp"),
                    jsonObj.getString("level"), jsonObj.getString("detail"));
        } else {
            LOG.info("Update db with  fCount = {}, fLastTime = {}, fEndTIme = {}, fContent = {}", count,
                    jsonObj.getDate("timestamp"), jsonObj.getDate("timestamp"), jsonObj.getString("detail"));
        }
    }
}