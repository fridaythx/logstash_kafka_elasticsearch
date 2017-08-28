package com.friday.esearch;

public interface ElasticSearchAdminAPI {
    void deleteIndexByName(String indexName)throws Exception;
}