package com.friday.esearch;

import java.util.List;
import java.util.Map;

import com.friday.entity.DelayValueDTO;
import com.friday.entity.Keyword;
import com.friday.entity.KeywordCount;

/**
 * Elasticsearch 查询
 * @author Friday
 *
 */
public interface ElasticSearchAPI {
	public KeywordCount countKeyword(Keyword ...keyword) throws Exception;

	public Map<String,List<DelayValueDTO>> queryByCondition(Map<String, Object> con,String ...indices) throws Exception;
}