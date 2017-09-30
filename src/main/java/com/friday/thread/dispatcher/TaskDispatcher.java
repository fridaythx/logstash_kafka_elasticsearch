package com.friday.thread.dispatcher;

import com.friday.thread.TaskSource;
import com.friday.thread.dispatcher.impl.AsyncTaskDispatch;
import com.friday.thread.dispatcher.impl.SyncTaskDispatch;

public class TaskDispatcher {
	
	private TaskDispatcher() {
	}
	
    private TaskDispatch asyncTaskDispatch = new AsyncTaskDispatch();

    private TaskDispatch syncTaskDispatch = new SyncTaskDispatch();

    public void dispatchTaskAsync(TaskSource taskSrc) throws RuntimeException {
        asyncTaskDispatch.dispatchTask(taskSrc);
    }

    public void dispatchTaskSync(TaskSource taskSrc) throws RuntimeException {
        syncTaskDispatch.dispatchTask(taskSrc);
    }
    
    private static TaskDispatcher instance = new TaskDispatcher(); 
    
    public static synchronized TaskDispatcher getSingleton() {
    		return instance;
    }
}