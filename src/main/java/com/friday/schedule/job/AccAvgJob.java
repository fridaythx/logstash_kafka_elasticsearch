package com.friday.schedule.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.friday.entity.DelayValueDTO;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.LogSearchType;
import com.friday.thread.constant.MessageDefinition;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatch;
import com.friday.thread.dispatcher.impl.AsyncTaskDispatch;

/**
 * 计算延时定时任务
 * @author Friday
 *
 */
public class AccAvgJob implements org.quartz.Job {
	private static final Logger LOG = LoggerFactory.getLogger(AccAvgJob.class);

	private static final String KEY_ROOT = "data";

	private static final String KEY_CLIENT_TO_VS = "clientToVs";

	private static final String KEY_VS_TO_SERVER = "vsToServer";

	private static final String KEY_TIME = "time";

	private static final String KEY_DEV_IP = "devIp";
	private static final String KEY_IP_PORT = "ipPort";
	private static final String KEY_DELAY_TIMES = "delayTimes";

	private ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();
	private TaskDispatch taskDispatch = new AsyncTaskDispatch();

	public AccAvgJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOG.info("AccAvgJob is executing.");
		Properties appProps = (Properties) context.getJobDetail().getJobDataMap().get("appProps");
		String delayTimeInSec = appProps.getProperty("schedule.job.accAvgJob.delayTimeInSec", "5");
		Map<String, Object> con = new HashMap<String, Object>();
		con.put("searchType", LogSearchType.DELAY_LOG_LIKE.getCode());
		con.put("dateRangeFrom", "now-" + delayTimeInSec + "s");
		con.put("dateRangeTo", "now");
		try {
			Map<String, List<DelayValueDTO>> queryByCondition = elasticSearchAPI.queryByCondition(con);

			JSONObject rs = calcEachHostDelayValue(queryByCondition);
			
			if(!rs.getJSONArray(KEY_ROOT).isEmpty()) {
				TaskSource taskSrc = new TaskSource(TaskType.DelayValueNotifyTask);
				taskSrc.setTaskEntity(rs);
				taskDispatch.dispatchTask(taskSrc);	
			}else {
				LOG.info("Skip empty dealy value data set.");
			}
		} catch (Exception e) {
			LOG.error(MessageDefinition.UNEXPECTED_ERROR.appendDesc("While calculating the delayVal."), e);
		}

		// 即收集5秒内传过来的延时值，然后去除头尾2%，取中间96%做平均，延时按收到的日志格式收集client到vs和vs到所有server的延时，前者以vs的ip+port为索引，后者以vs和member的ip+port为索引

	}

	private JSONObject calcEachHostDelayValue(Map<String, List<DelayValueDTO>> listMap) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Date now = new Date();
		jsonObject.put(KEY_ROOT, jsonArray);
		for (Entry<String, List<DelayValueDTO>> entry : listMap.entrySet()) {
			JSONObject hostDelayValue = new JSONObject();
			List<DelayValueDTO> list = entry.getValue();
			LOG.info("list[{}] : {}", list.size(), list);
			// 去hits 2%
			list = strip(list);
			// 取剩下数据平均值
			Map<String, Double> vsAvgValue = calcVsDelayAvgValue(list);
			LOG.info("vsAvgValue : {}", vsAvgValue);
			Map<String, Double> serverAvgValue = calcServerDelayAvgValue(list);
			LOG.info("serverAvgValue : {}", serverAvgValue);
			hostDelayValue.put(KEY_DEV_IP, entry.getKey());
			hostDelayValue.put(KEY_TIME, now);
			hostDelayValue.put(KEY_CLIENT_TO_VS, transformForDataMapping(vsAvgValue));
			hostDelayValue.put(KEY_VS_TO_SERVER, transformForDataMapping(serverAvgValue));
			jsonArray.add(hostDelayValue);
		}

		return jsonObject;
	}

	private Object transformForDataMapping(Map<String, Double> map) {
		JSONArray jsonArray = new JSONArray();
		for (Entry<String, Double> entry : map.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_IP_PORT, entry.getKey());
			jsonObject.put(KEY_DELAY_TIMES, entry.getValue());
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public List<DelayValueDTO> strip(List<DelayValueDTO> list) {
		// do not strip the list if the length of the list is less than 50
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
			String key = delayValueDTO.getVsAddress() + "|" + delayValueDTO.getServerAddress();
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