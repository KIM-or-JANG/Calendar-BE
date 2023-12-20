#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/cicdproject
cd $REPOSITORY

APP_NAME=Calendar-0.0.1
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
# nohup java -jar $JAR_PATH > --logging.file.path=/home/ec2-user/ --logging.level.org.hibernate.SQL=DEBUG >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &

# 백그라운드에서 실행하고 로그를 nohup.out 파일에 저장
nohup java -jar /home/ec2-user/Calendar/build/libs/Calendar-0.0.1-SNAPSHOT.jar > /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &
