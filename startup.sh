#!/bin/bash

doTest="true"


while getopts "tr" opts
do
    case $opts in
    t)
        echo "do test."
        doTest="false"
	mvn clean test
	exit
        ;;
    r)
        echo "do run."
        doRun="true"
        ;;
    esac
done
if [ -n "$doRun" ];
then
    echo "run. ${doRUn}"
    mvn -Dmaven.test.skip=true clean package	
    nohup java -jar ./target/log-filter-1.0-SNAPSHOT.jar &
fi

