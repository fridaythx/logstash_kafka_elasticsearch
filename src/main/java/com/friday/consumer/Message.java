package com.friday.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Message {

    private ConsumerRecord<String,String> record;
    public Message(ConsumerRecord<String, String> record) {
        this.record = record;
    }

    public String getContent() {
        return record.value();
    }

    public long getOffset(){
        return record.offset();
    }

    /**
     * @return the record
     */
    public ConsumerRecord<String, String> getRecord() {
        return record;
    }
}