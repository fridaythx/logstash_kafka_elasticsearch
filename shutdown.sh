ps -ef|grep log-filter|head -1|awk '{print }'|xargs kill -9
