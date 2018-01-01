package com.friday;

import com.friday.httpserver.HttpServerHandler;
import com.friday.schedule.MyScheduler;
import com.friday.thread.BasicLogicWorker;
import com.friday.thread.WorkerDeamonThread;
import com.friday.thread.WorkerSpawner;

import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * App 启动类
 * 
 * @author Friday
 *
 */
public class Bootstrap {
	private final static Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
	private static Properties appProps;

	public static void appStart(Properties properties) {
		appProps = properties;
		// 打印应用配置
		printAppProps();
		// 启动worker进程
		startWorkerThread();
		LOG.info("WorkerThread started...");
		// 初始化定时任务
		startScheduler();
		LOG.info("Scheduler started...");
		// 启动HTTP端口监听
		startHttpServerListener();
		LOG.info("HTTPServer listener started...");
	}

	private static void startWorkerThread() {
		// 启动worker守护线程&初始化启动worker线程
		new Thread(new WorkerDeamonThread(new BasicLogicWorkerSpawner())).start();
	}

	private static void startHttpServerListener() {
		String port = appProps.getProperty("httpserver.port");
		try {
			if (StringUtils.isNotEmpty(port)) {
				Server server = new Server(Integer.parseInt(port));
				server.setHandler(new HttpServerHandler());
				server.start();
				server.join();
			} else {
				LOG.info("HTTP SERVER won't run as no port specified.");
			}
		} catch (Exception e) {
			LOG.error(String.format("Failed to start HttpServer at port {}.", port), e);
		}

	}

	private static void startScheduler() {
		try {
			MyScheduler.getInstance(appProps).registerDataCleanJob().registerAccAvgJob().start();
		} catch (Exception e) {
			LOG.error("Failed to start scheduler.", e);
		}
	}

	public static void printAppProps() {
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		for (Entry<Object, Object> kv : appProps.entrySet()) {
			sb.append(kv.getKey() + " = " + kv.getValue() + lineSeparator);

		}
		LOG.info("App props : {}{}", lineSeparator, sb);
	}

	static class BasicLogicWorkerSpawner implements WorkerSpawner {
		private Integer count = 1;

		public BasicLogicWorker spawn() {
			return new BasicLogicWorker(appProps, "Worker-" + count++);
		}
	}

}