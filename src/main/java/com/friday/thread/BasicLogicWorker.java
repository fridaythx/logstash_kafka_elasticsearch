package com.friday.thread;

import com.friday.consumer.DelegatingMessageHandle;
import com.friday.consumer.Message;
import com.friday.consumer.MessageConsumer;
import com.friday.thread.constant.TaskType;
import com.friday.thread.dispatcher.TaskDispatcher;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 逻辑Worker线程
 * 主要对Kafka消费者吃到的消息进行处理
 * 吃到消息后封装为TaskSource后，taskDispacher分发任务
 * 
 * @author Friday
 *
 */
public class BasicLogicWorker implements Runnable, DelegatingMessageHandle {
	private static final Logger LOG = LoggerFactory.getLogger(BasicLogicWorker.class);
	private volatile boolean running = true;
	private String identifier;
	private Properties appProps;
	private MessageConsumer consumer;
	private TaskDispatcher taskDispatcher = TaskDispatcher.getSingleton();

	public BasicLogicWorker(Properties appProps, String identifier) {
		this.appProps = appProps;
		this.identifier = identifier;
		consumer = new MessageConsumer(this);
	}

	public void run() {
		try {
			consumer.init(appProps);
			while (running) {
				try {
					consumer.consume();
				} catch (Exception e) {
					LOG.error("Unexpected error occurred during consuming records", e);
					stop();
				}
			}
		} catch (Exception e) {
			LOG.error("Worker exception.", e);
		}
	}

	public void stop() {
		running = false;
		consumer.stop();
		LOG.info("Worker [{}] stopped.", identifier);
	}
	
	public boolean isRunning() {
		return running;
	}

	public void handleMessage(Message message) throws Exception {
		//分发任务
		taskDispatcher.dispatchTaskSync(new TaskSource(message, TaskType.PreLogicTask, taskDispatcher));
	}
}