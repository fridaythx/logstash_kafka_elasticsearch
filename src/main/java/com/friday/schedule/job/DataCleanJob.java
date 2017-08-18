package com.friday.schedule.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCleanJob implements org.quartz.Job{
    private static final Logger LOG = LoggerFactory.getLogger(DataCleanJob.class);
    public DataCleanJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Hello World!  DataCleanJob is executing.");
        LOG.info("Now time: " + System.currentTimeMillis());
    }
}