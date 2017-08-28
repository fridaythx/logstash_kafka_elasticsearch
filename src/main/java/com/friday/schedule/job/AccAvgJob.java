package com.friday.schedule.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.entity.dto.DelayValueDTO;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.thread.constant.LogType;
import com.friday.thread.dispatcher.AsyncTaskDispatch;
import com.friday.thread.dispatcher.TaskDispatch;

public class AccAvgJob implements org.quartz.Job {
	private static final Logger LOG = LoggerFactory.getLogger(AccAvgJob.class);
	private ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();
	private TaskDispatch taskDispatch = new AsyncTaskDispatch();

	public AccAvgJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("AccAvgJob is executing.");
		Properties appProps = (Properties) context.getJobDetail().getJobDataMap().get("appProps");
		String delayTimeInSec = appProps.getProperty("schedule.job.accAvgJob.delayTimeInSec", "5");
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("searchType", LogType.DELAY_LOG_LIKE);
		con.put("dateRangeFrom", "now-" + delayTimeInSec + "s");
		con.put("dateRangeTo", "now");
		try {
			List<DelayValueDTO> list = elasticSearchAPI.queryByCondition(con);
			LOG.info("list[{}] : {}", list.size(), list);
			// 去hits 2%
			list = strip(list);
			// 取剩下数据平均值
			Map<String, Double> vsAvgValue = calcVsDelayAvgValue(list);
			LOG.info("vsAvgValue : {}", vsAvgValue);
			Map<String, Double> serverAvgValue = calcServerDelayAvgValue(list);
			LOG.info("serverAvgValue : {}", serverAvgValue);
			// TaskSource taskSrc = new TaskSource(TaskType.PreLogicTask);
			// taskDispatch.dispatchTask(taskSrc);
		} catch (Exception e) {
			LOG.error("Failed", e);
		}

		// 即收集5秒内传过来的延时值，然后去除头尾2%，取中间96%做平均，延时按收到的日志格式收集client到vs和vs到所有server的延时，前者以vs的ip+port为索引，后者以vs和member的ip+port为索引

	}

	public List<DelayValueDTO> strip(List<DelayValueDTO> list) {
		// do not strip the list if the len of the list is less than 50
		if (list.size() < 50) {
			return list;
		}

		int percentage2 = (int) (list.size() * 0.02);
		LOG.info("Striped list size : {}", list.size());
		LOG.info("Striped num : {}", percentage2);
		LOG.info("Strip first, sublist from {} to {}", percentage2, list.size() - 1);
		// strip first 2 percent data
		list = list.subList(percentage2, list.size() - 1);
		LOG.info("Strip last, sublist from {} to {}", 0, list.size() - 1 - percentage2);
		// strip last 2 percent data
		list = list.subList(0, list.size() - 1 - percentage2);
		return list;
	}

	public Map<String, Double> calcVsDelayAvgValue(List<DelayValueDTO> list) {
		Map<String, List<DelayValueDTO>> temp = new HashMap<String, List<DelayValueDTO>>();
		for (DelayValueDTO delayValueDTO : list) {
			String key = delayValueDTO.getVsAddress();
			if (!temp.containsKey(key)) {
				temp.put(key, new ArrayList<DelayValueDTO>());
			}
			temp.get(key).add(delayValueDTO);
		}
		Map<String, Double> result = new HashMap<String, Double>();
		Double acc = 0d;
		for (Entry<String, List<DelayValueDTO>> entry : temp.entrySet()) {
			for (DelayValueDTO delayValueDTO : entry.getValue()) {
				acc += Double.parseDouble(delayValueDTO.getToVsDelayVal());
			}
			result.put(entry.getKey(), acc / entry.getValue().size());
		}

		return result;
	}

	public Map<String, Double> calcServerDelayAvgValue(List<DelayValueDTO> list) {
		Map<String, List<DelayValueDTO>> temp = new HashMap<String, List<DelayValueDTO>>();
		for (DelayValueDTO delayValueDTO : list) {
			String key = delayValueDTO.getVsAddress() + delayValueDTO.getServerAddress();
			if (!temp.containsKey(key)) {
				temp.put(key, new ArrayList<DelayValueDTO>());
			}
			temp.get(key).add(delayValueDTO);
		}
		Map<String, Double> result = new HashMap<String, Double>();
		Double acc = 0d;
		for (Entry<String, List<DelayValueDTO>> entry : temp.entrySet()) {
			for (DelayValueDTO delayValueDTO : entry.getValue()) {
				acc += Double.parseDouble(delayValueDTO.getToVsDelayVal());
			}
			result.put(entry.getKey(), acc / entry.getValue().size());
		}
		return result;
	}
}