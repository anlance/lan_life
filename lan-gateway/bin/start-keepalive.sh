#!/bin/sh

# Module name
modulename="gateway"

logfile=/logs/operation.log
projectPath="/opt/java-service/${modulename}"
binfile="${projectPath}/bin"
jarFile="${projectPath}/$modulename.jar"

echo "[`date`] start keeper ${modulename} ..." | tee -a $logfile

# Start service
nohup sh ${binfile}/start-service.sh ${modulename} > /dev/null 2>&1 &
echo "continue." | tee -a $logfile

while true
do
	sleep 10;
	proc_num=`ps -ef |grep ${jarFile} |grep -v "grep" | wc -l`
	# echo "proc_num: $proc_num"
	if [ $proc_num -eq 0 ]
	then
		echo "[`date`] ${modulename} dead, do restart..." | tee -a $logfile
		nohup sh ${binfile}/start-service.sh ${modulename} > /dev/null 2>&1 &
	fi
done
