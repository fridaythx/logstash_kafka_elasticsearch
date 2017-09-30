package com.friday.dal.mapper;

import java.util.Map;

import com.friday.entity.output.Log;

/**
 * Log mapper used to deal with db operations.
 * 
 * @author Friday
 *
 */
public interface LogMapper {
	Long selectCount();

	void insert(Log log);

	void update(Map<String, Object> params);
	
	Long queryDevIdByDevIp(String ip);
}