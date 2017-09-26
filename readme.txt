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

---------------
append below text to /etc/sysctl.conf 
vm.max_map_count=262144
sudo sysctl -p

append below text to /etc/security/limits.conf
*    soft nofile 65536
*    hard nofile 65536
root soft nofile 65536
root hard nofile 65536


/etc/pam.d/common-session
session required pam_limits.so

/etc/pam.d/common-session-noninteractive
session required pam_limits.so

-------------


elasticsearch_head_plugin
git clone git://github.com/mobz/elasticsearch-head.git
cd elasticsearch-head
npm install 
npm start


Run chmod +x ./startup.sh && ./startup.sh to start this java program.

Setting JAVA and MAVEN env variables manually may be necessary for you to run this program.




curl -XGET 'localhost:9200/_template/logstash_1?pretty'

curl -XDELETE 'localhost:9200/_template/logstash_1?pretty'


curl -XPUT 'localhost:9200/_template/logstash_1?pretty' -H 'Content-Type: application/json' -d'
{
    "template": "logstash-*",
    "order": 1,
    "mappings": {
        "_default_": {
            "_all": {
                "enabled": true,
                "omit_norms": true
            },
            "dynamic_templates": [
                {
                    "message_field": {
                        "path_match": "message",
                        "mapping": {
                            "norms": false,
                            "type": "text"
                        },
                        "match_mapping_type": "string"
                    }
                },
                {
                    "string_fields": {
                        "mapping": {
                            "type": "string",
                            "index": "not_analyzed",
                            "doc_values": true
                        },
                        "match_mapping_type": "string",
                        "match": "*"
                    }
                }
            ]
        }
    }
}
'
