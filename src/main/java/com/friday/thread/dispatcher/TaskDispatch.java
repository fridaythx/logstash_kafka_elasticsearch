package com.friday.thread.dispatcher;

import com.friday.thread.TaskSource;

public interface TaskDispatch {
    void dispatchTask(TaskSource taskSrc) throws RuntimeException;
}