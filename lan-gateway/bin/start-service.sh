#!/bin/sh  

# Module name
modulename="$1"

projectPath="/opt/java-service/${modulename}"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/bootstrap.yml"
JAVA_OPTS="-Xms1024m -Xmx1024m -Xss512k -Djava.rmi.server.hostname=192.168.0.111 -Dcom.sun.management.jmxremote.port=1099
-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/java-service/jvmlogs/
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintReferenceGC -XX:+PrintGCApplicationStoppedTime
-XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -Xloggc:/opt/java-service/jvmlogs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=14
-XX:GCLogFileSize=100M"
pidpath="/var/run/${modulename}.pid"
JRE_HOME="/opt/jdk/jre/bin/java"

# kill service process
tpid=`ps -ef | grep $jarFile | grep -v "grep" | awk '{print $2}'`
echo "Kill pid ${tpid}."
if [ "${tpid}" ]; then  
    kill -9 $tpid 
    rm -f $pidPath
fi

# create file
workingDir=$(cd `dirname $0`;pwd)
workingDir=`dirname $workingDir`
cd $workingDir
cd ..
if [ ! -d "logs/startup" ]; then
    mkdir -p logs/startup
fi

# start service
#nohup $JRE_HOME $JAVA_OPTS -javaagent:/opt/monitor/jmx_exporter/jmx_prometheus_javaagent.jar=15316:/opt/monitor/jmx_exporter/config.yaml -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > /dev/null 2>&1 &
#nohup $JRE_HOME $JAVA_OPTS -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > logs/startup/${modulename}.log 2>&1 &
nohup $JRE_HOME $JAVA_OPTS -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > /dev/null 2>&1 &
echo $! > $pidpath

# open log
if [ "$1" = "log" ]; then
    tail -f logs/${modulename}/${modulename}.log
fi
if [ "$1" = "log2" ]; then
    tail -f logs/startup/${modulename}.log
fi
echo "Program is starting."