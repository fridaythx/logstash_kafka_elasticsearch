package com.friday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.friday.consumer.Message;
import com.friday.dal.SqlSessionFactory;
import com.friday.dal.mapper.LogMapper;
import com.friday.entity.DelayValueDTO;
import com.friday.entity.Keyword;
import com.friday.entity.MessageDTO;
import com.friday.entity.output.Log;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchAdminAPI;
import com.friday.esearch.ElasticSearchConnector;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.esearch.impl.ElasticSearchAdminAPIImpl;
import com.friday.schedule.MyScheduler;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.LogLevel;
import com.friday.thread.constant.LogSearchType;
import com.friday.thread.constant.LogType;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;
import com.friday.thread.task.PreLogicTask;
import com.friday.utils.DateUtil;
import com.friday.utils.HttpUtil;

/**
 * Unit test for simple App.
 */
public class AppTest {

	/**
	 * Rigourous Test :-)
	 */
	@Test
	@Ignore
	public void testESearch() {
		try {
			ElasticSearchAPI api = new ElasticSearchAPIImpl();
			api.countKeyword(new Keyword("fullContent.keyword",
					"debug mcpd[6508]: 01070468:7: eXtremeDB free pages: 61798, total pages: 146912, page_size: 128\n"),
					new Keyword("dateRangeFrom", DateUtil.date2Utc(DateUtil.calc(new Date(), DateUtil.FIELD_HOUR, -2))),
					new Keyword("dateRangeTo", DateUtil.date2Utc(DateUtil.calc(new Date(), DateUtil.FIELD_HOUR, -1))));
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

	@Test
	@Ignore

	public void testRangeSearch() {
		try {
			ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();
			Map<String, Object> con = new HashMap<String, Object>();
			con.put("searchType", LogSearchType.DELAY_LOG_LIKE.getCode());
			// data pushed in elasticsearch in recent one minute
			con.put("dateRangeFrom", "now-1d");
			con.put("dateRangeTo", "now");
			Map<String, List<DelayValueDTO>> listMap = elasticSearchAPI.queryByCondition(con);
			System.out.println(listMap);
		} catch (Exception e) {
			throw new RuntimeException("failed.");
		}
	}

	@Test
	@Ignore
	public void testAccAvgJob() {
		try {
			Properties props = new Properties();
			props.setProperty("schedule.job.accAvgJobCron", "0/5 * * * * ? *");
			MyScheduler.getInstance(props).registerAccAvgJob().start();
			Thread.sleep(1000000);
		} catch (Exception e) {
			throw new RuntimeException("failed.");
		}
	}

	@Ignore
	@Test
	public void testHTTPClient() {
		try {
			String string = HttpUtil.postForm("http://www.baidu.com", null);
			System.out.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testDb() {
		try {
			SqlSessionFactory factory = SqlSessionFactory.getInstance();
			SqlSession sqlSession = factory.getSqlSession();
			LogMapper mapper = sqlSession.getMapper(LogMapper.class);
			Long queryDevIdByDevIp = mapper.queryDevIdByDevIp("192.168.1.250");
			Log log = new Log();
			log.setfContent("test message.");
			log.setfCount(1);
			log.setDevId(queryDevIdByDevIp);
			log.setfEndTime(new Timestamp(System.currentTimeMillis()));
			log.setfStartTime(new Timestamp(System.currentTimeMillis()));
			log.setfLastTime(new Timestamp(System.currentTimeMillis()));
			log.setfFacility((short) 1);
			log.setfLevel(LogLevel.DEBUG.getCode());
			log.setfStatus((short) 1);
			log.setfType(LogType.AUDIT_LOG.getCode());
			mapper.insert(log);
			System.out.println("id:" + log.getFid());
			long select = mapper.selectCount();
			System.out.println("count:" + select);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("fCount", 2);
			params.put("fLastTime", new Timestamp(System.currentTimeMillis()));
			params.put("fEndTime", new Timestamp(System.currentTimeMillis()));
			params.put("startTime", new Timestamp(System.currentTimeMillis()));
			params.put("endTime", new Timestamp(System.currentTimeMillis()));
			params.put("content", "");
			params.put("devId", "1");
			mapper.update(params);
			factory.closeSqlSession(sqlSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testDateUtil() {
		Date now = new Date();
		Date calc = DateUtil.calc(now, DateUtil.FIELD_HOUR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(now));
		System.out.println(sdf.format(calc));
	}

	@Test
	@Ignore
	public void testMatchData() {
		PreLogicTask task = new PreLogicTask(null);
		MessageDTO systemLog = new MessageDTO();
		systemLog.setFacility((short) 1);
		systemLog.setSeverity((short) 4);
		systemLog.setMessage("123");
		System.out.println("match systemlog : " + task.matchSystemLog(systemLog));

		MessageDTO ltmLog = new MessageDTO();
		ltmLog.setFacility((short) 16);
		ltmLog.setMessage("123");
		System.out.println("match ltmlog : " + task.matchLtmLog(ltmLog));

		MessageDTO gtmLog = new MessageDTO();
		gtmLog.setFacility((short) 18);
		System.out.println("match gtmlog : " + task.matchGtmLog(gtmLog));

		MessageDTO auditLog = new MessageDTO();
		auditLog.setFacility((short) 16);
		auditLog.setMessage("123 AUDIT");
		System.out.println("match auditlog : " + task.matchAuditLog(auditLog));
	}

	@Test
	@Ignore
	public void testFlow() {
		TaskDispatcher dispatcher = TaskDispatcher.getSingleton();
		TaskSource taskSrc = new TaskSource(new Message(null) {
			@Override
			public String getContent() {
				try {
					BufferedReader fis = new BufferedReader(
							new InputStreamReader(AppTest.class.getClassLoader().getResourceAsStream("json.dat")));
					try {
						return fis.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						fis.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "{}";
			}
		}, TaskType.PreLogicTask, dispatcher);
		dispatcher.dispatchTaskSync(taskSrc);
	}

	@Test
	@Ignore
	public void testGetUTC() {
		Date utc2Date;
		try {
			utc2Date = DateUtil.utc2Date("2017-09-20T17:22:49.000Z");
			System.out.println(utc2Date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	@Ignore
	public void testRegex() {
		String content = "{\"severity\":5,\"@timestamp\":\"2017-12-31T13:59:01.000Z\",\"fullContent\":\"notice tmsh[27871]: 01420002:5: AUDIT - pid=27871 user=root folder=/Common module=(tmos)# status=[Command OK] cmd_data=show sys mcp-state field-fmt\\n\",\"@version\":\"1\",\"host\":\"192.168.1.250\",\"message\":\" 123 : SSHPLUGIN AUDIT 435 \\n\",\"priority\":133,\"logsource\":\"bigip1\",\"facility\":16,\"severity_label\":\"Notice\",\"timestamp\":\"2017-12-31T13:59:01.000Z\",\"facility_label\":\"local0\"}";

		System.out.println(content);

		MessageDTO messageDTO = JSON.parseObject(content, MessageDTO.class);

		PreLogicTask preLogicTask = new PreLogicTask(null);

		boolean matchLtmLog = preLogicTask.matchLtmLog(messageDTO);
		
		boolean matchAuditLog = preLogicTask.matchAuditLog(messageDTO);
		
		System.out.println(messageDTO.getMessage());

		System.out.println("matchLtmLog " + matchLtmLog);
		
		System.out.println("matchAuditLog " + matchAuditLog);
	}
}
