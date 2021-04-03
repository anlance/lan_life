#!/bin/sh

modulename="basic"
cd /opt/java-service/${modulename}/bin

DATE=`date`
echo "[$DATE] start monitor ${modulename} ..."

sh /opt/java-service/${modulename}/bin/start.sh
if [ "$1" = "log" ]; then
    tail -f logs/${modulename}/${modulename}.log
fi

echo "continue."
while true
do
	sleep 10;
	proc_num=`ps -ef |grep /opt/java-service/${modulename} |grep -v "grep" | wc -l`
	echo "proc_num: $proc_num"
	DATE=`date`
	if [ $proc_num -eq 0 ]
	then
		echo "[$DATE] ${modulename} dead, do restart..."
		sh /opt/java-service/${modulename}/bin/start.sh
	else
		echo "[$DATE] continue."
	fi
done
