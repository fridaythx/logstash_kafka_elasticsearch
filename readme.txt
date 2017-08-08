Logstash
bin/logstash -f first-pipeline.conf --config.test_and_exit
bin/logstash -f first-pipeline.conf --config.reload.automatic
nohup bin/logstash -f logstash-simple.conf > my_logstash.log &

https://www.elastic.co/guide/en/logstash/5.5/advanced-pipeline.html#configuring-filebeat

Kafka
Start zookepper
nohup bin/zookeeper-server-start.sh config/zookeeper.properties > my_zookeepper.log &
Start kafka server
nohup bin/kafka-server-start.sh config/server.properties > my_kafkaserver.log &


Create a topic 
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

Take a look
bin/kafka-topics.sh --list --zookeeper localhost:2181

send msg from stdin
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

Start a consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
