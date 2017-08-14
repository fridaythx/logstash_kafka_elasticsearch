package com.friday.esearch;

public interface ElasticSearchAPI {
    public long countKeyword(String key, String value) throws Exception;
}