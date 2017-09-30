package com.friday.thread;


import com.friday.consumer.Message;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;

/**
 * 任务实体
 * @author Friday
 *
 */
public class TaskSource{
    private TaskType taskType;

    private Message source;

    private Object taskEntity;

    private TaskDispatcher taskDispatcher;

    public TaskSource(Message source, TaskType taskType) {
        this(taskType);
        this.source = source;
    }

    public TaskSource(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskSource(Message message, TaskType taskType,TaskDispatcher taskDispatcher) {
        this(message, taskType);
        this.taskDispatcher = taskDispatcher;
    }

    /**
     * @return the taskType
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * @return the taskEntity
     */
    public Object getTaskEntity() {
        return taskEntity;
    }

    /**
     * @return the source
     */
    public Message getSource() {
        return source;
    }

    /**
     * @return the taskDispatcher
     */
    public TaskDispatcher getTaskDispatcher() {
        return taskDispatcher;
    }

    /**
     * @param taskEntity the taskEntity to set
     */
    public void setTaskEntity(Object taskEntity) {
        this.taskEntity = taskEntity;
    }
}