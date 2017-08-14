package com.friday.thread.task;

import com.friday.thread.TaskSource;
import com.friday.thread.constant.TaskType;

public class DataCleanTask extends BasicTask{
    public DataCleanTask(){
        super(new TaskSource(TaskType.DataCleanTask));
    }
    @Override
    public void taskRun() {
        
    }
}