package com.friday.consumer;

/**
 * Kafka 消息处理
 * @author Friday
 *
 */
public interface DelegatingMessageHandle {
    void handleMessage(Message message) throws Exception;
}