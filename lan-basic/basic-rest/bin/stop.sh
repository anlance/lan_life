#!/bin/sh

moduleName="basic"

echo "Stop service ${moduleName}."

#if [ -f "/var/run/${moduleName}.pid" ]; then
#    tpid=`cat /var/run/${moduleName}.pid | awk '{print $1}'`  
#    tpid=`ps -aef | grep $tpid | awk '{print $2}' |grep $tpid`  
#    if [ ${tpid} ]; then  
#        kill -9 $tpid 
#        rm -f /var/run/${moduleName}.pid 
#    fi  
#fi

kpid=`ps -ef | grep ${moduleName}/bin/start-keepalive.sh | grep -v "grep" | awk '{print $2}'`
echo "kill keepalive pid ${kpid}."
if [ "${kpid}" ]; then  
    kill -9 $kpid 
fi 

tpid=`ps -ef | grep /${moduleName}/${moduleName}.jar | grep -v "grep" | awk '{print $2}'`
echo "kill pid ${tpid}."
if [ "${tpid}" ]; then  
    kill -15 $tpid 
    rm -rf /var/run/${moduleName}.pid 
fi 

tpid=`ps -ef | grep /${moduleName}/${moduleName}.jar | grep -v "grep" | awk '{print $2}'`
i=0
while true
do
    if [ "${tpid}" ]; then  
        if [ $i -ge 8 ]; then  
            echo "kill -9 ${tpid}".
            kill -9 $tpid
            break
        fi 
        let i++
        echo "Wait $i seconds."
        sleep 1s
        tpid=`ps -ef | grep /${moduleName}/${moduleName}.jar | grep -v "grep" | awk '{print $2}'`
    else
        echo "Process not exsit."
        break
    fi
done

echo "Program stopped."