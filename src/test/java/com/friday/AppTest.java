package com.friday;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

import com.friday.entity.dto.DelayValueDTO;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchAdminAPI;
import com.friday.esearch.ElasticSearchConnector;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.esearch.impl.ElasticSearchAdminAPIImpl;
import com.friday.schedule.MyScheduler;
import com.friday.thread.constant.LogType;

/**
 * Unit test for simple App.
 */
public class AppTest {

	/**
	 * Rigourous Test :-)
	 */
	@Ignore
	@Test
	public void testESearch() {
		try {
			ElasticSearchAPI api = new ElasticSearchAPIImpl();
			api.countKeyword("detail.keyword",
					"011a4002:1: SNMP_TRAP: Pool /Common/lua.midea.com.cn member lua-huaian_midea_com_cn (ip:port=10.159.1.251:7070) state change red --> green");
			api.countKeyword("targetHost.keyword", "GDSD-PDC-Intranet-GTM2");
			api.countKeyword("level.keyword", "alert");
			ElasticSearchConnector.getSingletonInstance().disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("failed.");
		}
	}

	@Ignore
	@Test
	public void testScheduler() {
		try {
			Properties props = new Properties();
			props.setProperty("schedule.job.dataCleanJobCron", "0 0 * * * ? *");
			MyScheduler.getInstance(props).registerDataCleanJob().start();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("failed.");
		}

	}

	@Ignore
	@Test
	public void testDeleteIndex() {
		try {
			ElasticSearchAdminAPI elasticSearchAdminAPI = new ElasticSearchAdminAPIImpl();
			elasticSearchAdminAPI.deleteIndexByName("logstash-2017.08.27");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("failed.");
		}
	}

	@Ignore
	@Test
	public void testJobDeleteIndex() {
		try {
			Properties props = new Properties();
			props.put("schedule.job.dataCleanJob.keepDays", "10");
			props.setProperty("schedule.job.dataCleanJobCron", "0/1 * * * * ?");
			MyScheduler.getInstance(props).registerDataCleanJob().start();
			Thread.sleep(1000000);
		} catch (Exception e) {
			throw new RuntimeException("failed.");
		}
	}

	@Ignore
	@Test
	public void testRegx() {
		assert true != "Delay 10 10 127.0.0.1 127.0.0.1".matches("(?!Delay).*");
		assert true == "Aug 12:00:00".matches("(?!Delay).*");
	}

	@Ignore
	@Test
	public void testRangeSearch() {
		try {
			ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();
			Map<String, Object> con = new HashMap<String, Object>();
			con.put("searchType", LogType.DELAY_LOG_LIKE);
			// data pushed in elasticsearch in recent one minute
			con.put("dateRangeFrom", "now-1m");
			con.put("dateRangeTo", "now");
			List<DelayValueDTO> list = elasticSearchAPI.queryByCondition(con);
			System.out.println(list);
		} catch (Exception e) {
			throw new RuntimeException("failed.");
		}
	}

	@Test
	public void testAccAvgJob() {
		try {
			Properties props = new Properties();
			props.setProperty("schedule.job.accAvgJobCron", "0/5 * * * * ?");
			MyScheduler.getInstance(props).registerAccAvgJob().start();
			Thread.sleep(1000000);
		} catch (Exception e) {
			throw new RuntimeException("failed.");
		}
	}

}
