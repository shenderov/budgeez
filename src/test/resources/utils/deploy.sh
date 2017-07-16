#!/bin/bash

timeout=500

result="Build successfully deployed"

function waiting {
time=0
valid_code=200
while true
do
code=$(curl -m 2 -s -o /dev/null -I -w "%{http_code}" -X GET 'http://localhost:8080/budgeez/general/getGeneralChartSelectionsList')
        if [ "${code}" -ne "${valid_code}" ] && [ "${timeout}" -gt "${time}" ]
        then
        count=5
		time=$((time+count))
        while [ ${count} -gt 0 ]
        do
                echo -n ' .'
                count=$((count-1))
                sleep 1
        done
		elif [ ${time} -ge ${timeout} ]
			then
				echo
				result="Build failed due to waiting timeout"
				break
        else
				echo
                break
        fi
done
}

deploy_start=$(date +%s.%N)
echo "Stop Tomcat"
sudo -i -u cubie /home/cubie/kamabizbazti/bin/shutdown.sh
echo "Delete old build"
sudo -i -u cubie rm -rf /home/cubie/kamabizbazti/webapps/kamabizbazti/
sudo -i -u cubie rm -f /home/cubie/kamabizbazti/webapps/kamabizbazti
echo "Copying the new build"
sudo -i -u cubie cp /var/lib/jenkins/jobs/Kamabizbazti_1_0/workspace/target/kamabizbazti-1.0.0-SNAPSHOT.war /home/cubie/kamabizbazti/webapps/kamabizbazti.war
echo "Starting Tomcat..."
sudo -i -u cubie /home/cubie/kamabizbazti/bin/startup.sh
echo "Waiting for application startup:"
waiting
deploy_end=$(date +%s.%N)
deploy_runtime=$(python -c "print(${deploy_end} - ${deploy_start})")
echo -n "${result}"
echo " in $deploy_runtime sec"