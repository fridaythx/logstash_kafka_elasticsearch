package com.friday.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * worker守护线程
 * 用于当worker线程挂掉之后，检测重启worker线程
 * @author Friday
 *
 */
public class WorkerDeamonThread implements Runnable {
	private static Logger LOG = LoggerFactory.getLogger(WorkerDeamonThread.class);
	private WorkerSpawner workerSpawner;

	private BasicLogicWorker worker;

	public WorkerDeamonThread(WorkerSpawner workerSpawner) {
		this.workerSpawner = workerSpawner;
		worker = workerSpawner.spawn();
		new Thread(worker).start();
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(1000 * 10);
				if (!worker.isRunning()) {
					LOG.info("Worker is not running.");
					worker = workerSpawner.spawn();
					LOG.info("Spawned a new worker.");
					LOG.info("Try to start new worker.");
					new Thread(worker).start();
					LOG.info("New worker started.");
				} else {
					LOG.info("Worker is alive.");
				}
			}
		} catch (Exception e) {
			LOG.error("WorkerDeamonThread is dead.", e);
		}
	}

}
