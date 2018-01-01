#!/bin/bash

jar_path=`ls ./target/log-filter-*.jar`

env="dev"

while getopts "trp" opts
do
    case $opts in
    t)
        echo "do test."
		mvn clean test
        ;;
    r)
        echo "do run" $jar_path
        rm -f ./nohup.out
		mvn -Dmaven.test.skip=true clean package && nohup java -jar $jar_path &
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
