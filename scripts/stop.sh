#!/usr/bin/env bash

PROJECT_ROOT="/home/ec2-user"
JAR_FILE="$PROJECT_ROOT/mejai-gg.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f $JAR_FILE)

# 프로세스가 켜져 있으면 종료
if [ -z $CURRENT_PID ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> $DEPLOY_LOG
  exit 0
fi

echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료 시도" >> $DEPLOY_LOG
kill -15 $CURRENT_PID

TIMEOUT=30
for i in $(seq 1 $TIMEOUT); do
    if ! pgrep -f "$JAR_FILE"; then
        echo "$TIME_NOW > $CURRENT_PID 애플리케이션 종료 완료" >> "$DEPLOY_LOG"
        exit 0
    fi
    echo "$TIME_NOW > 종료 대기 중... ($i/$TIMEOUT)" >> "$DEPLOY_LOG"
    sleep 1
done

# 30초내로 종료 실패하면 에러
exit 1
