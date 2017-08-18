mvn clean package -Dmaven.test.skip=true
nohup java -jar ./target/log-filter-1.0-SNAPSHOT.jar &
