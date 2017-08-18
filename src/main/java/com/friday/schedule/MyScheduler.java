package com.friday.schedule;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.schedule.job.AccAvgJob;
import com.friday.schedule.job.DataCleanJob;

import org.quartz.Trigger;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.util.Properties;

import static org.quartz.CronScheduleBuilder.*;
import org.quartz.JobDetail;

import static org.quartz.SimpleScheduleBuilder.*;

public class MyScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(MyScheduler.class);
    private static MyScheduler scheduler;

    private Scheduler _scheduler;

    private Properties properties;

    private MyScheduler() {

    }

    public static MyScheduler getInstance(Properties properties) throws Exception {
        if (scheduler == null) {
            scheduler = new MyScheduler();
            scheduler.properties = properties;
            scheduler.init();
        }
        return scheduler;
    }

    private void init() throws Exception {
        // Grab the Scheduler instance from the Factory
        _scheduler = StdSchedulerFactory.getDefaultScheduler();
        // and start it off
        scheduler.start();
    }

    public MyScheduler registerDataCleanJob() {
        JobDetail job = newJob(DataCleanJob.class).withIdentity("job1", "group1").build();
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow()
                .withSchedule(cronSchedule(properties.getProperty("schedule.job.dataCleanJobCron", "0 0 * * * ? *")))
                .forJob(job).build();
        try {
            _scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOG.error("Failed to register DataCleanJob.", e);
        }

        return this;
    }

    public MyScheduler registerAccAvgJob() {
        JobDetail job = newJob(AccAvgJob.class).withIdentity("job2", "group1").build();
        Trigger trigger = newTrigger().withIdentity("trigger2", "group1").startNow()
                .withSchedule(cronSchedule(properties.getProperty("schedule.job.dataCleanJobCron", "0 0 * * * ? *")))
                .forJob(job).build();
        try {
            _scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOG.error("Failed to register AccAvgJob.", e);
        }
        return this;
    }

    public void start() throws Exception {
        //_scheduler.scheduleJob(jobDetail, trigger)
        _scheduler.start();
    }

    public void stop() throws Exception {
        _scheduler.shutdown();
    }
}