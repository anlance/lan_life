#!/bin/sh  

# Module name
modulename="config"
JAVA_OPTS="-Xms64m -Xmx64m -Xss256k"
JRE_HOME="/opt/jdk/jre/bin/java"
projectPath="/opt/java-service/${modulename}"
binfile="${projectPath}/bin"

# kill keeper
kpid=`ps -ef | grep ${modulename}/bin/start-keepalive.sh | grep -v "grep" | awk '{print $2}'`
echo "Kill keepalive pid ${kpid}."
if [ "${kpid}" ]; then  
    kill -9 $kpid 
fi 
if [ ! -d "logs/${modulename}" ]; then
    mkdir -p logs/${modulename}
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