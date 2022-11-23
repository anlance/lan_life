#!/bin/sh  

modulename="gateway"
projectPath="/opt/java-service/$modulename"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/bootstrap.yml"
JAVA_OPTS="-Xms256m -Xmx256m -Xss256k -XX:PermSize=128m -XX:MaxNewSize=256m -XX:MaxPermSize=256m"
JRE_HOME="/opt/jdk/jre/bin/java"
pidpath="/var/run/${modulename}.pid"

address=8019
if [[ $# > 0 ]] && [[ $1 =~ [0-9]+ ]]; then
    address=$1
fi

# kill service process
tpid=`ps -ef | grep $jarFile | grep -v "grep" | awk '{print $2}'`
echo "Kill pid ${tpid}."
if [ "${tpid}" ]; then
    kill -9 $tpid
    rm -f $pidPath
fi
rm -f $pidPath

workingDir=$(cd `dirname $0`;pwd)
workingDir=`dirname $workingDir`
cd $workingDir
cd ..
if [ ! -d "logs/startup" ]; then
    mkdir -p logs/startup
fi
nohup $JRE_HOME $JAVA_OPTS -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$address,suspend=n -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > logs/startup/${modulename}.log 2>&1 &
#nohup $JRE_HOME $JAVA_OPTS -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$address,suspend=n -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > /dev/null 2>&1 &

echo $! > $pidpath

echo "Program is starting."
tail -f logs/startup/${modulename}.log