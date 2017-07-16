#!/bin/bash

database=$DB_NAME
database_host=$DB_HOST
database_username=$DB_USERNAME
database_password=$DB_PASSWORD
tomcat_start_command="/etc/init.d/tomcat8 start"
tomcat_stop_command="/etc/init.d/tomcat8 stop"
build_package_name="budgeez-1.0.war"
tmp_dir="/home/budgeez/tmp/"
get_version_url="http://localhost:8080/general/getVersion"
timeout=500
version_validated=false

function waiting {
time=0
valid_code=200
while true
do
code=$(curl -m 2 -s -o /dev/null -I -w "%{http_code}" -X GET "${get_version_url}")
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
				echo "Roll back"
                roll_back
				exit 1
        else
				echo
				break
        fi
done
}

function deploy {
    cd $WORKSPACE
    jar -xvf "${build_package_name}"
    rm -rf WEB-INF/classes/application.properties
    cp $CONFIG_FILE WEB-INF/classes/application.properties
    jar -uvf "${build_package_name}" WEB-INF/classes/application.properties
    rm -rf META-INF/ org/ WEB-INF/
    sudo mv $WORKSPACE/"${build_package_name}" $WEBAPP_PATH$TARGET_FILE_NAME
}

function validate_version {
    build_properties=$WORKSPACE/build.properties
    source <(grep -v '^ *#' "${build_properties}" | grep '[^ ] *=' | awk '{split($0,a,"="); print gensub(/\./, "_", "g", a[1]) "=" a[2]}')
    war_version="${build_version}"."${build_number}"."${build_timestamp}"
    installed_version=$(curl -s "${get_version_url}" | jq -r '.version').$(curl -s "${get_version_url}" | jq -r '.timestamp')
    if [ "${war_version}" == "${installed_version}" ];
        then
        echo Installed successfully
        version_validated=true
    fi
    echo Buld version: "${war_version}"
    echo Installed version: "${installed_version}"
}

function roll_back {
    # Stop Tomcat
    echo "Stop Tomcat"
    sudo "${tomcat_stop_command}"

    # Restore old build from backup
    echo "Restore old build from backup"
    sudo cp "${tmp_dir}"$TARGET_FILE_NAME.PRODUCTION_BACKUP $WEBAPP_PATH$TARGET_FILE_NAME

    # Restore DB from dump
    echo "Restore DB from dump"
    mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" < "${tmp_dir}""${database}".sql

    # Start Tomcat
    echo "Start tomcat"
    sudo "${tomcat_start_command}"
    sleep 5
    echo "Waiting for application startup:"
    waiting

    echo "Deployment failed, old version was recovered"
    exit 1
}

# Stop Tomcat
echo "Stop Tomcat"
sudo "${tomcat_stop_command}"

# Backup previous build
echo "Backup previous war file"
sudo cp $WEBAPP_PATH$TARGET_FILE_NAME "${tmp_dir}"$TARGET_FILE_NAME.PRODUCTION_BACKUP

# Take DB dump
echo "Take DB dump"
mysqldump -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" > "${tmp_dir}""${database}".sql

# Deploying new build
echo "Deploying new build"
deploy
echo "Start tomcat"
sudo "${tomcat_start_command}"
sleep 5
echo "Waiting for application startup:"
waiting

# Validate new version
echo "Validate new version"
validate_version

# Roll back if failed
if [ "${version_validated}" = false ]
    then
    echo "Roll back"
    roll_back
    exit 1
fi

jenkins    ALL = NOPASSWD: /bin/mv /var/lib/jenkins/workspace/*/* /var/lib/tomcat8/webapps/*
jenkins    ALL = NOPASSWD: /bin/cp /home/kamabizbazti/tmp/* /var/lib/tomcat8/webapps/*
jenkins    ALL = NOPASSWD: /bin/cp /var/lib/tomcat8/webapps/* /home/kamabizbazti/tmp/*
jenkins    ALL = NOPASSWD: /etc/init.d/tomcat8 *