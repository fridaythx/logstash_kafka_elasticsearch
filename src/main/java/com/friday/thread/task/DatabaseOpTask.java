package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.friday.entity.po.FLog;
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
        FLog log = (FLog) taskSrc.getTaskEntity();
        int count = log.getCount();
        if (count == 0) {
            LOG.info(
                    "Insert into db with fType = normal,fCount = {}, fStartTime = {}, fLastTime = {}, fEndTime = {}, fStatus = {}, fContent = {}",
                    count, log.getStartTime(), log.getLastTime(), log.getEndTime(),
                    log.getStatus(), log.getContent());
        } else {
            LOG.info("Update db with  fCount = {}, fLastTime = {}, fEndTime = {}, fContent = {}", count,
            log.getCount(), log.getLastTime(), log.getEndTime());
        }
    }
}