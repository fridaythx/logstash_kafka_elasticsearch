package com.friday.thread.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.friday.entity.Keyword;
import com.friday.entity.KeywordCount;
import com.friday.entity.MessageDTO;
import com.friday.entity.output.Log;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.thread.TaskSource;
import com.friday.thread.constant.LogType;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;
import com.friday.utils.DateUtil;

/**
 * 预处理任务 首先执行完毕后会分发其他三种类型的任务
 * 
 * @author Friday
 *
 */
public class PreLogicTask extends BasicTask {
	private static final Logger LOG = LoggerFactory.getLogger(PreLogicTask.class);

	private ElasticSearchAPI elasticSearchAPI = new ElasticSearchAPIImpl();

	private TaskSource taskSrc;

	public PreLogicTask(TaskSource taskSrc) {
		super(taskSrc);
		this.taskSrc = taskSrc;
	}

	public void taskRun() throws RuntimeException {
		TaskDispatcher taskDispatcher = taskSrc.getTaskDispatcher();
		// if it is the kind of alert.
		String content = taskSrc.getSource().getContent();
		LOG.info("Message content : {}", content);
		MessageDTO messageDTO = JSON.parseObject(content, MessageDTO.class);

		if (messageDTO.getType() != null) {
			// escape delay calc message
			LOG.info("Escape delay calc message.");
			return;
		}

		Log log = new Log();
		log.setfLevel((short) (messageDTO.getSeverity() + 1));
		log.setDevIP(messageDTO.getHost());
		log.setfEndTime(messageDTO.getTimestamp());
		log.setfLastTime(log.getfEndTime());
		log.setfFacility(messageDTO.getFacility());
		log.setfStatus((short) 1);
		log.setfContent(messageDTO.getFullContent());
		//the fullContent field will be null if this message was not matched by the Logstash filter "split-message-body" 
		//if(StringUtils.isEmpty(messageDTO.getFullContent())) {
		//	log.setfContent(messageDTO.getMessage());
		//}
		
		// determining the type of the log.
		if (matchSystemLog(messageDTO)) {
			log.setfType(LogType.SYSTEM_LOG.getCode());
		} else if (matchLtmLog(messageDTO)) {
			log.setfType(LogType.LTM_LOG.getCode());
		} else if (matchGtmLog(messageDTO)) {
			log.setfType(LogType.GTM_LOG.getCode());
		} else if (matchAuditLog(messageDTO)) {
			log.setfType(LogType.AUDIT_LOG.getCode());
		} else {
			log.setfType(LogType.UNRECOGNIZED.getCode());
		}

		try {
			// query all log messages that are repeated any times in recent one hour.
			KeywordCount countKeyword = elasticSearchAPI.countKeyword(
					new Keyword("fullContent.keyword", messageDTO.getFullContent()),
					new Keyword("host.keyword", messageDTO.getHost()),
					new Keyword("dateRangeFrom",
							DateUtil.date2Utc(DateUtil.calc(messageDTO.getTimestamp(), DateUtil.FIELD_HOUR, -1))),
					new Keyword("dateRangeTo", DateUtil.date2Utc(messageDTO.getTimestamp())));
			log.setfCount((int) (countKeyword.getCount() == 0 ? 1 : countKeyword.getCount()));
			if (countKeyword.getStartTime() == null) {
				log.setfStartTime(messageDTO.getTimestamp());
			} else {
				log.setfStartTime(countKeyword.getStartTime());
			}

			TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.DbOpTask,
					taskSrc.getTaskDispatcher());

			newTaskSource.setTaskEntity(log);
			taskDispatcher.dispatchTaskSync(newTaskSource);
		} catch (Exception e) {
			LOG.error("An error occurred during using elasticsearch.", e);
			throw new RuntimeException("An error occurred during using elasticsearch.");
		}

		if (messageDTO.getSeverity() >= 0 && messageDTO.getSeverity() <= 3) {
			// dispatchAlertTask
			TaskSource newTaskSource = new TaskSource(taskSrc.getSource(), TaskType.AlertTask,
					taskSrc.getTaskDispatcher());
			newTaskSource.setTaskEntity(messageDTO);
			taskDispatcher.dispatchTaskAsync(newTaskSource);
		}

	}

	public boolean matchSystemLog(MessageDTO dto) {
		if (!matchAny(dto.getFacility(), 0, 2, 4, 7, 9, 10, 15, 16, 17, 18, 19, 20, 21, 22, 23)
				&& matchAny(dto.getSeverity(), 4, 5) && !dto.getMessage().matches("WA")) {
			return true;
		}
		return false;
	}

	public boolean matchLtmLog(MessageDTO dto) {
		if (matchAny(dto.getFacility(), 16) && !dto.getMessage().matches("AUDIT")
				&& !dto.getMessage().matches("msgbusd:|msgbusd.sh:|msgbusd\\[[0-9]+\\]:")
				&& !dto.getMessage().matches("icrd:|icrd_child:|icrd\\[[0-9]+\\]:|icrd_child\\[[0-9]+\\]:")
				&& !dto.getMessage().matches(": 246415[34][890]{1} ")
				&& !dto.getMessage().matches(": 017[cC][0-9a-fA-F]{4}") && !dto.getMessage().matches("SSHPLUGIN")) {
			return true;
		}
		return false;
	}

	public boolean matchGtmLog(MessageDTO dto) {
		if (matchAny(dto.getFacility(), 18)) {
			return true;
		}
		return false;
	}

	public boolean matchAuditLog(MessageDTO dto) {
		if (matchAny(dto.getFacility(), 16) && dto.getMessage().matches("AUDIT")) {
			return true;
		}
		return false;
	}

	public boolean matchAny(short val, int... matches) {
		for (int i : matches) {
			if (val == i) {
				return true;
			}
		}
		return false;
	}
}