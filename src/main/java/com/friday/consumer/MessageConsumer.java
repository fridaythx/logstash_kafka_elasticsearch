package com.friday.consumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka 消息消费者
 * @author Friday
 *
 */
public class MessageConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

    private DelegatingMessageHandle handler;

    private KafkaConsumer<String, String> consumer;

    public MessageConsumer(DelegatingMessageHandle messageHandle) {
        this.handler = messageHandle;
    }

    public void init(Properties appProps) {
        Properties props = new Properties();
        props.put("bootstrap.servers", appProps.get("kafka.bootstrap.servers"));
        props.put("group.id", appProps.get("kafka.group.id"));
        props.put("enable.auto.commit", appProps.get("kafka.enable.auto.commit"));
        props.put("key.deserializer", appProps.get("kafka.key.deserializer"));
        props.put("value.deserializer", appProps.get("kafka.value.deserializer"));
        String topics = (String) appProps.get("kafka.topics");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topics.split(",")));
        LOG.info("Consumer initialized...");
    }

    public void consume() {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        if (records.count() > 0) {
            LOG.info("Batch of records size : {}", records.count());
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                try {
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        handler.handleMessage(new Message(record));
                    }
                } catch (Exception e) {
                    long firstOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    LOG.warn(String.format(
                            "An exception occurred during handling message, do not call commit. partition [%s] rollback position => %d",
                            partition, firstOffset), e);
                    continue;
                }
                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                LOG.info(
                        "Batch of records have been all consumed successfully, do commit. partition [{}] committed position => {}",
                        partition, lastOffset);
                commit(partition, lastOffset + 1);
            }
        }
    }

    private void commit(TopicPartition partition, long offset) {
        consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset)));
    }

    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }
}
