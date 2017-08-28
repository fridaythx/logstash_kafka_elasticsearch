package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.friday.entity.dto.MessageDTO;
import com.friday.entity.po.FLog;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.LogType;
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
        MessageDTO messageDTO = JSON.parseObject(content, MessageDTO.class);
        
        	if(messageDTO.getType() != null) {
        		//escape delay calc message
        		LOG.info("Escape delay calc message.");
        		return;
        	}
        
        if (messageDTO.getSeverity() >= 0 && messageDTO.getSeverity() <= 3) {
            //  dispatchAlertTask
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.AlertTask,
                    taskSrc.getTaskDispatcher());
            newTaskSource.setTaskEntity(messageDTO);
            taskDispatcher.dispatchTaskAsync(newTaskSource);
        }

        FLog log = new FLog();
        log.setContent(messageDTO.getMessage());
        log.setLastTime(messageDTO.getTimestamp());
        // determining the type of the log.
        //system
        if (matchSystemLog(messageDTO)) {
            log.setType(LogType.SYSTEM_LOG);
        } else if (matchLtmLog(messageDTO)) {
            log.setType(LogType.LTM_LOG);
        } else if (matchGtmLog(messageDTO)) {
            log.setType(LogType.GTM_LOG);
        } else if (matchAuditLog(messageDTO)) {
            log.setType(LogType.AUDIT_LOG);
        } else {
            log.setType(LogType.UNRECOGNIZED);
        }

        try {
            long count = elasticSearchAPI.countKeyword("message.keyword", messageDTO.getMessage());
            log.setCount((int) count);
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.DbOpTask,
                    taskSrc.getTaskDispatcher());
            newTaskSource.setTaskEntity(log);
            taskDispatcher.dispatchTaskSync(newTaskSource);
        } catch (Exception e) {
            LOG.error("An error occurred during using elasticsearch.", e);
            throw new RuntimeException("An error occurred during using elasticsearch.");
        }

    }

    public boolean matchSystemLog(MessageDTO dto) {
        if (matchAny(dto.getFacility(), 0, 2, 4, 7, 9, 10, 15, 16, 17, 18, 19, 20, 21, 22, 23)
                && matchAny(dto.getSeverity(), 4, 5) && !dto.getMessage().matches("WA")) {
            return true;
        }
        return false;
    }

    public boolean matchLtmLog(MessageDTO dto) {
        if (matchAny(dto.getFacility(), 16) && !dto.getMessage().matches("AUDIT")
                && !dto.getMessage().matches("msgbusd:|msgbusd.sh:|msgbusd\\[[0-9]+\\]:")
                && !dto.getMessage().matches("icrd:|icrd_child:|icrd\\[[0-9]+\\]:|icrd_child\\[[0-9]+\\]:")
                && !dto.getMessage().matches(": 246415[34][890]{1} ")
                && !dto.getMessage().matches(": 017[cC][0-9a-fA-F]{4}") && !dto.getMessage().matches("SSHPLUGIN")) {
            return true;
        }
        return false;
    }

    public boolean matchGtmLog(MessageDTO dto) {
        if (matchAny(dto.getFacility(), 18)) {
            return true;
        }
        return false;
    }

    public boolean matchAuditLog(MessageDTO dto) {
        if (matchAny(dto.getFacility(), 16) && dto.getMessage().matches("AUDIT")) {
            return true;
        }
        return false;
    }

    public boolean matchAny(short val, int... matches) {
        for (int i : matches) {
            if (val == i) {
                return true;
            }
        }
        return false;
    }
}