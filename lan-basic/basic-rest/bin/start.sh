#!/bin/sh  

modulename="basic"
projectPath="/opt/java-service/${modulename}"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/conf/bootstrap.yml"
JAVA_OPTS="-Xms256m -Xmx256m -Xss256k"
pidpath="/var/run/${modulename}.pid"
JRE_HOME="/opt/jdk/jre/bin/java"
binfile="${projectPath}/bin"

kpid=`ps -ef | grep /${modulename}/bin/start-keepalive.sh | grep -v "grep" | awk '{print $2}'`
echo "Kill keepalive pid ${kpid}."
if [ "${kpid}" ]; then
    kill -9 $kpid
fi

nohup sh ${binfile}/start-keepalive.sh ${modulename} > /dev/null 2>&1 &

# open log
if [ "$1" = "log" ]; then
    tail -f logs/${modulename}/${modulename}.log
fi
if [ "$1" = "log2" ]; then
    tail -f logs/startup/${modulename}.log
fi
echo "Program is starting."
