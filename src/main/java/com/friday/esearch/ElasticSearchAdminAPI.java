package com.friday.esearch;

/**
 * Elasticsearch 维护
 * @author Friday
 *
 */
public interface ElasticSearchAdminAPI {
    void deleteIndexByName(String indexName)throws Exception;
}