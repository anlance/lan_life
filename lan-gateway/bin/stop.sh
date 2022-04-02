#!/bin/sh

modulename="gateway"

echo "Stop service ${modulename}."

#if [ -f "/var/run/${modulename}.pid" ]; then
#    tpid=`cat /var/run/${modulename}.pid | awk '{print $1}'`
#    tpid=`ps -aef | grep $tpid | awk '{print $2}' |grep $tpid`
#    if [ ${tpid} ]; then
#        kill -9 $tpid
#        rm -f /var/run/${modulename}.pid
#    fi
#fi

# kill keeper
kpid=`ps -ef | grep /${modulename}/bin/start-keepalive.sh | grep -v "grep" | awk '{print $2}'`
echo "Kill keepalive pid ${kpid}."
if [ "${kpid}" ]; then
    kill -9 $kpid
fi

# force kill process
if [ "$1" = "force" ]; then
    tpid=`ps -ef | grep /${modulename}/${modulename}.jar | grep -v "grep" | awk '{print $2}'`
    echo "Force kill pid ${tpid}."
    if [ "${tpid}" ]; then
        kill -9 $tpid
    fi
    rm -rf /var/run/${modulename}.pid
    exit 0
fi

# kill process
tpid=`ps -ef | grep /${modulename}/${modulename}.jar | grep -v "grep" | awk '{print $2}'`
echo "Kill pid ${tpid}."
if [ "${tpid}" ]; then
    kill -15 $tpid
fi

# force kill process
tpid=`ps -ef | grep /${modulename}/${modulename}.jar | grep -v "grep" | awk '{print $2}'`
i=0
while true
do
    if [ "${tpid}" ]; then
        if [ $i -ge 8 ]; then
            echo "Kill -9 ${tpid}".
            kill -9 $tpid
            break
        fi
        let i++
        echo "Wait $i seconds."
        sleep 1s
        tpid=`ps -ef | grep /${modulename}/${modulename}.jar | grep -v "grep" | awk '{print $2}'`
    else
        echo "Process not exsit."
        break
    fi
done

rm -rf /var/run/${modulename}.pid
echo "Program stopped."