#!/bin/sh  

modulename="basic"
projectPath="/opt/java-service/$modulename"
jarFile="${projectPath}/$modulename.jar"
configFile="${projectPath}/conf/bootstrap.yml"
JAVA_OPTS="-Xms1024m -Xmx1024m -Xss256k -XX:PermSize=128m -XX:MaxNewSize=128m -XX:MaxPermSize=256m"
JRE_HOME="/opt/jdk/jre/bin/java"
pidpath="/var/run/${modulename}.pid"

address=8019
if [[ $# > 0 ]] && [[ $1 =~ [0-9]+ ]]; then
    address=$1
fi

#sh ${projectPath}/bin/stop.sh
rm -f $pidPath

workingDir=$(cd `dirname $0`;pwd)
workingDir=`dirname $workingDir`

cd $workingDir
cd ..
if [ ! -d "logs/startup" ]; then
    mkdir -p logs/startup
fi
#nohup $JRE_HOME $JAVA_OPTS -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$address,suspend=n -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server > logs/startup/${modulename}.log 2>&1 &
$JRE_HOME $JAVA_OPTS -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=$address,suspend=n -jar -Duser.timezone=Asia/Shanghai -Dspring.config.location=$configFile $jarFile -server &

echo $! > $pidpath

echo "Program is starting."
tail -f logs/${modulename}/${modulename}.log