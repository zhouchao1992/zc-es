#!/usr/bin/env bash
app_name='zc-es'
docker stop ${app_name}
echo '----stop container----'
docker rm ${app_name}
echo '----rm container----'
docker run -p 8887:8887 --name ${app_name} \
--link mysql:db \
-v /etc/localtime:/etc/localtime \
-v /home/app/${app_name}/logs:/var/logs \
-d zc-learning/${app_name}:0.0.1-SNAPSHOT
echo '----start container----'