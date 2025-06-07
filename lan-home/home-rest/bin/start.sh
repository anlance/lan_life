#!/bin/sh  

modulename="home"
projectPath="/opt/java-service/${modulename}"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/conf/bootstrap.yml"
JAVA_OPTS="-Xms512m -Xmx512m -Xss256k -Djava.rmi.server.hostname=192.168.0.111 -Dcom.sun.management.jmxremote.port=1098
                                      -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/java-service/jvmlogs/
                                      -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintReferenceGC -XX:+PrintGCApplicationStoppedTime
                                      -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -Xloggc:/opt/java-service/jvmlogs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=14
                                      -XX:GCLogFileSize=100M"
pidpath="/var/run/${modulename}.pid"
JRE_HOME="/opt/jdk/jre/bin/java"

#sh ${projectPath}/bin/stop.sh
rm -f $pidPath

workingDir=$(cd `dirname $0`;pwd)
workingDir=`dirname $workingDir`

cd $workingDir
cd ..
if [ ! -d "logs/${modulename}" ]; then
    mkdir -p logs/${modulename}
fi
#nohup $JRE_HOME $JAVA_OPTS -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > logs/startup/${modulename}.log 2>&1 &
$JRE_HOME $JAVA_OPTS -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > /dev/null 2>&1 &

echo $! > $pidpath

echo "Program is starting."

if [ "$1" = "log" ]; then
    tail -f logs/${modulename}/${modulename}.log
fi
