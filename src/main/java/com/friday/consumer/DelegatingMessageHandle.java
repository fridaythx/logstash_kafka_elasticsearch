package com.friday.consumer;

public interface DelegatingMessageHandle {
    void handleMessage(Message message) throws Exception;
}