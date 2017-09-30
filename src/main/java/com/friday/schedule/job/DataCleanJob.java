package com.friday.schedule.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.esearch.ElasticSearchAdminAPI;
import com.friday.esearch.impl.ElasticSearchAdminAPIImpl;

/**
 * 清理Elasticsearch索引定时任务
 * @author Friday
 *
 */
public class DataCleanJob implements org.quartz.Job {
	private static final Logger LOG = LoggerFactory.getLogger(DataCleanJob.class);

	private static final String PREFIX = "logstash-";

	private ElasticSearchAdminAPI elasticSearchAdminAPI = new ElasticSearchAdminAPIImpl();

	public DataCleanJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("DataCleanJob is executing.");
		LOG.info("Now time: " + System.currentTimeMillis());
		Properties appProps = (Properties) context.getJobDetail().getJobDataMap().get("appProps");
		String keepDays = appProps.getProperty("schedule.job.dataCleanJob.keepDays", "7");
		LOG.info("keepDays:" + keepDays);
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(keepDays));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String indexName = PREFIX + simpleDateFormat.format(nowTime.getTime());
		LOG.info("IndexName {} to be deleted",indexName);
		try {
			elasticSearchAdminAPI.deleteIndexByName(indexName);
		} catch (Exception e) {
			LOG.error("Failed", e);
		}
	}
}