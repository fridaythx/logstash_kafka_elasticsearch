package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.entity.dto.MessageDTO;
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
        MessageDTO messageDTO = (MessageDTO) taskSrc.getTaskEntity();
        int count = messageDTO.getRepeatCount();
        if (count == 0) {
            LOG.info(
                    "Insert into db with fType = normal,fCount = {}, fStartTime = {}, fLastTime = {}, fEndTime = {}, fStatus = {}, fContent = {}",
                    count, messageDTO.getTimestamp(), messageDTO.getTimestamp(), messageDTO.getTimestamp(),
                    messageDTO.getSeverity(), messageDTO.getMessage());
        } else {
            LOG.info("Update db with  fCount = {}, fLastTime = {}, fEndTIme = {}, fContent = {}", count,
                    messageDTO.getTimestamp(), messageDTO.getTimestamp(), messageDTO.getMessage());
        }
    }
}