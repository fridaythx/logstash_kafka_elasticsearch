package com.friday.esearch;

public interface ElasticSearchDocumentAPI {
    void index();
    void get();
    void delete();
    void search();
}