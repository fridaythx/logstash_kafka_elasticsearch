package com.friday.thread.dispatcher;

import com.friday.thread.TaskSource;

public class TaskDispatcher {
    private TaskDispatch asyncTaskDispatch = new AsyncTaskDispatch();

    private TaskDispatch syncTaskDispatch = new SyncTaskDispatch();

    public void dispatchTaskAsync(TaskSource taskSrc) throws Exception {
        asyncTaskDispatch.dispatchTask(taskSrc);
    }

    public void dispatchTaskSync(TaskSource taskSrc) throws Exception {
        syncTaskDispatch.dispatchTask(taskSrc);
    }
}