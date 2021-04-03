#!/bin/sh  

modulename="gateway"
projectPath="/opt/java-service/${modulename}"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/bootstrap.yml"
JAVA_OPTS="-Xms1024m -Xmx1024m -Xss256k"
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
