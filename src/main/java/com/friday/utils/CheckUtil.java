package com.friday.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查看耗时工具类
 * @author Friday
 *
 */
public class CheckUtil {
	private static final Logger LOG = LoggerFactory.getLogger(CheckUtil.class);
	private static ThreadLocal<Map<String, Long>> temp = new ThreadLocal<Map<String, Long>>();

	public static void record(String key) {
		if (temp.get() == null) {
			temp.set(new HashMap<String, Long>());
		}
		temp.get().put(key, System.currentTimeMillis());
	}

	public static void reportTimeElapsed(String key) {
		Map<String, Long> map = temp.get();
		if (map != null && map.containsKey(key)) {
			Long start = map.get(key);
			LOG.info("CheckPoint [{}] time elapsed : {}ms", key, System.currentTimeMillis() - start);
		}
	}
}
