package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        JSONObject jsonObj = JSON.parseObject(content);
        String level = jsonObj.getString("level");
        String detail = jsonObj.getString("detail");
        if (LogLevel.LEVEL_ALERT.equals(level)) {
            //  dispatchAlertTask
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.AlertTask,
                    taskSrc.getTaskDispatcher());
            newTaskSource.setTaskEntity(jsonObj);
            taskDispatcher.dispatchTaskAsync(newTaskSource);
        }
        //{"path":"/Users/user4/Documents/test.log","networkGroup":"GDSD-PDC-Intranet-GTM2","@timestamp":"2017-08-13T06:03:12.503Z","level":"alert","@version":"1","host":"user4deMacBook-Pro.local","extra2":"011a4002:1:","extra3":"SNMP_TRAP:","extra1":"gtmd[17015]:","timestamp":"2017-08-04T07:36:32.000Z"}
        try {
            long count = elasticSearchAPI.countKeyword("detail.keyword", detail);
            TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.DbOpTask,
                    taskSrc.getTaskDispatcher());
            jsonObj.put("count", count);
            newTaskSource.setTaskEntity(jsonObj);
            taskDispatcher.dispatchTaskSync(newTaskSource);
        } catch (Exception e) {
            LOG.error("An error occurred during using elasticsearch.", e);
            throw new RuntimeException("An error occurred during using elasticsearch.");
        }

    }
}