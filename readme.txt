Logstash
bin/logstash -f logstash-simple.conf --config.test_and_exit
bin/logstash -f logstash-simple.conf --config.reload.automatic
nohup bin/logstash -f logstash-simple.conf > my_logstash.log &

https://www.elastic.co/guide/en/logstash/5.5/advanced-pipeline.html#configuring-filebeat

Kafka
Start zookeper
nohup bin/zookeeper-server-start.sh config/zookeeper.properties > my_zookeepper.log &
Start kafka server
nohup bin/kafka-server-start.sh config/server.properties > my_kafkaserver.log &
nohup bin/kafka-server-start.sh config/server-1.properties > my_kafkaserver1.log &


Create a topic 
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 2 --partitions 1 --topic my-replicated-topic

Take a look
bin/kafka-topics.sh --list --zookeeper localhost:2181
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic

send msg from stdin
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic

Start a consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-replicated-topic --from-beginning
elasticsearch
start esearch
nohup bin/elasticsearch > my_esearch.log &
ulimit -n 65536
or
/etc/security/limits.conf

check max_file_descriptors
curl -XGET 'localhost:9200/_nodes/stats/process?filter_path=**.max_file_descriptors&pretty'

setting number of threads
/etc/security/limits.conf
nproc


elasticsearch_head_plugin
git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install 
npm start


Run chmod +x ./startup.sh && ./startup.sh to start this java program.

Setting JAVA and MAVEN env variables manually may be necessary for you to run this program.