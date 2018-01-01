#!/bin/bash

BASE_PATH="/home/shengan"

LOG_FILE_PATH="$BASE_PATH/elk_boot.log"

SOFTWARE_DIR="$BASE_PATH/elk"

E_DIR="$SOFTWARE_DIR/elasticsearch-5.5.1"

ES_BOOT_LOG_FILE_PATH="$E_DIR/my_esearch.log"

L_DIR="$SOFTWARE_DIR/logstash-5.5.1"

K_DIR="$SOFTWARE_DIR/kafka_2.11-0.11.0.0"

log()
{
    echo `date "+%Y-%m-%d %H:%M:%S"` $1 >> $LOG_FILE_PATH
    echo ""
}

watch()
{
    watch_path=$1
    watch_kw=$2

    if [ ! -f $watch_path ]; then
        log "$watch_path not existed or not a file."
        return 1;
    fi

    if [ -z $watch_kw ]; then
        log "watch kw cannot be empty or null"
        return 1;
    fi

    while true
    do
        log "sleep for 3 seconds.."

        sleep 3

        log "run grep $watch_kw $watch_path"

        matched=`grep $watch_kw $watch_path`

        log "matched text => $matched"

        if [ -n "$matched" ]; then
            break;
        fi
    done
}

boot_elasticsearch()
{
    log "booting elastic search.."
    #首先启动elasticsearch
    nohup $E_DIR/bin/elasticsearch > $ES_BOOT_LOG_FILE_PATH 2>&1 &
    
    log "sleep for a second."

    sleep 1

    watch $ES_BOOT_LOG_FILE_PATH "started"

    if [ "$?" = "0" ]; then
        log "elastic search have done started successfully."
    else
        log "elastic search could not be booted."
	exit 1
    fi
}


boot_kafka()
{
    log "booting kafka..."
    #启动zookeeper
    nohup $K_DIR/bin/zookeeper-server-start.sh $K_DIR/config/zookeeper.properties > $K_DIR/my_zookeepper.log 2>&1 &

    log "waiting for zoo-keeper startup for 10 seconds."

    sleep 10

    log "start kafka1 now.."
    #启动成功后启动kafka
    nohup $K_DIR/bin/kafka-server-start.sh $K_DIR/config/server.properties > $K_DIR/my_kafkaserver.log 2>&1 &

    log "start kafka2 now.."
    
    nohup $K_DIR/bin/kafka-server-start.sh $K_DIR/config/server-1.properties > $K_DIR/my_kafkaserver1.log 2>&1 &

    log "kafka finished booting without checking out boot log."
}

boot_logfilter()
{
    log "start log-filter.."

    nohup java -jar $SOFTWARE_DIR/log-filter-1.0-SNAPSHOT.jar > /dev/null 2>&1 &

    log "log filter started."
}

boot_logstash()
{
    log "boot logstash"
    #启动log-filter
    #启动logstash
    nohup $L_DIR/bin/logstash -f $L_DIR/logstash-simple.conf > /dev/null 2>&1 &
    
    log "logstash finished booting without checking out boot log."
}




boot()
{
    if [ -f $LOG_FILE_PATH ]; then
        rm -f $LOG_FILE_PATH
        log "done deleting log file."
    fi
    boot_elasticsearch
    boot_kafka
    boot_logfilter
    boot_logstash
}

boot
