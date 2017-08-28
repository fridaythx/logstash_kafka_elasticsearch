package com.friday.esearch;

import java.util.List;
import java.util.Map;

import com.friday.entity.dto.DelayValueDTO;

public interface ElasticSearchAPI {
	public long countKeyword(String key, String value) throws Exception;

	public List<DelayValueDTO> queryByCondition(Map<String, Object> con,String ...indices) throws Exception;
}