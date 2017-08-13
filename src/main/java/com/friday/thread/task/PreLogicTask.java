package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.thread.TaskSource;
import com.friday.thread.dispatcher.TaskDispatcher;

/**
 * 预处理任务
 * 首先执行完毕后会分发其他三种任务类型
 */
public class PreLogicTask extends BasicTask {
    private static final Logger LOG = LoggerFactory.getLogger(PreLogicTask.class);
    
    private TaskSource taskSrc;

    public PreLogicTask(TaskSource taskSrc) {
        super(taskSrc);
        this.taskSrc = taskSrc;
    }

    public void taskRun() {
        TaskDispatcher taskDispatcher = taskSrc.getTaskDispatcher();
        //if it is kind of alert.
        //  dispatchAlertTask
        //taskDispatcher.dispatchTask();
        //search in esearch 
        //if not existed
        //  dispatchInsertTask
        //taskDispatcher.dispatchTask();
        //else
        // dispatchUpdateTask
        //taskDispatcher.dispatchTask();

    }
}