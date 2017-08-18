package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.friday.entity.dto.MessageDTO;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.LogLevel;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;

/**
 * 预处理任务
 * 首先执行完毕后会分发其他三种任务类型
 */
public class PreLogicTask extends BasicTask {
    private static final Logger LOG = LoggerFactory.getLogger(PreLogicTask.class);

    private ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();

    private TaskSource taskSrc;

    public PreLogicTask(TaskSource taskSrc) {
        super(taskSrc);
        this.taskSrc = taskSrc;
    }

    public void taskRun() throws RuntimeException {
        TaskDispatcher taskDispatcher = taskSrc.getTaskDispatcher();
        //if it is kind of alert.
        String content = taskSrc.getSource().getContent();
        LOG.info("Message content : {}", content);
        MessageDTO messageDTO = JSON.parseObject(content,MessageDTO.class);
        if(messageDTO.getSeverity() >= 0 && messageDTO.getSeverity() <= 3){
            //  dispatchAlertTask
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.AlertTask,
                    taskSrc.getTaskDispatcher());
            newTaskSource.setTaskEntity(messageDTO);
            taskDispatcher.dispatchTaskAsync(newTaskSource);
        }
        try {
            long count = elasticSearchAPI.countKeyword("message.keyword", messageDTO.getMessage());
            messageDTO.setRepeatCount((int)count);
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.DbOpTask,
                    taskSrc.getTaskDispatcher());
            newTaskSource.setTaskEntity(messageDTO);
            taskDispatcher.dispatchTaskSync(newTaskSource);
        } catch (Exception e) {
            LOG.error("An error occurred during using elasticsearch.", e);
            throw new RuntimeException("An error occurred during using elasticsearch.");
        }

    }
}