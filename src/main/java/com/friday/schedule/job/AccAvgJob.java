package com.friday.schedule.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccAvgJob implements org.quartz.Job {
    private static final Logger LOG = LoggerFactory.getLogger(DataCleanJob.class);

    public AccAvgJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Hello World!  AccAvgJob is executing.");
    }
}