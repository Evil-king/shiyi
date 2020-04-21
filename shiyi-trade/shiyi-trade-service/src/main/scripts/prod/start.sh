#!/bin/sh
#变量设置
SERVICE_NAME=shiyi-trade
mkdir -p /logs/$SERVICE_NAME
CONFIG_DIR=/apps/$SERVICE_NAME/config
LIB_DIR=/apps/$SERVICE_NAME/lib
LIB_JARS=`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":"`
MAIN_CLASS="com.baibei.shiyi.trade.TradeServiceApplication"
mkdir -p  /logs/$SERVICE_NAME/
# 设置classpath
java -Dapp.id=shiyi-trade -Denv=dev -Dapollo.cluster=default -Dapollo.meta=http://10.10.0.39:8080 -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS | tee -a /logs/$SERVICE_NAME/std-out.log