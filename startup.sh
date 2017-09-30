#!/bin/bash

env="dev"

while getopts "trp" opts
do
    case $opts in
    t)
        echo "do test."
		mvn clean test
        ;;
    r)
        echo "do run."
		mvn -Dmaven.test.skip=true clean package && nohup java -jar ./target/log-filter-1.0-SNAPSHOT.jar &
        ;;
	p)
		echo "do package prd version."
		mvn -Dmaven.test.skip=true -Pprd clean package
		;;
	e)
		echo "do package dev version."
		mvn -Dmaven.test.skip=true -Pdev clean package
		;;
    esac
done
echo "done"
