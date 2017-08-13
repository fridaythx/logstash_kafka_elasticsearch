package com.friday.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.friday.consumer.Message;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;

public class TaskSource {
    private TaskType taskType;

    private ConsumerRecord<String, String> source;

    private Object taskEntity;

    private TaskDispatcher taskDispatcher;

    public TaskSource(ConsumerRecord<String, String> source, TaskType taskType) {
        this.source = source;
    }

    public TaskSource(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskSource(Message message, TaskType taskType,TaskDispatcher taskDispatcher) {
        this(message.getRecord(), taskType);
        this.taskDispatcher = taskDispatcher;
    }

    /**
     * @return the taskType
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * @param taskType the taskType to set
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
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
    public ConsumerRecord<String, String> getSource() {
        return source;
    }

    /**
     * @return the taskDispatcher
     */
    public TaskDispatcher getTaskDispatcher() {
        return taskDispatcher;
    }
}