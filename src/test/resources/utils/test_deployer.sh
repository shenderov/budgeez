#!/bin/bash

database=$DB_NAME
database_host=$DB_HOST
database_username=$DB_USERNAME
database_password=$DB_PASSWORD
application_server_host=$SERVER_HOST
application_server_username=$SERVER_USERNAME
application_server_password=$SERVER_PASSWORD
application_server_port=$TOMCAT_PORT
application_webapp_path=$WEBAPP_PATH
application_target_filename=$TARGET_FILE_NAME
tomcat_manager_username=$TOMCAT_MANAGER_USERNAME
tomcat_manager_password=$TOMCAT_MANAGER_PASSWORD
tomcat_context="/"
is_upgrade=$UPGRADE_TEST
load_test_data=$LOAD_TEST_DATA
workspace=$WORKSPACE
config_file=$CONFIG_FILE
build_properties=$WORKSPACE/build.properties
build_package_name="budgeez-1.1.war"
names_database="/home/kamabizbazti/tools/names_database.csv"
users_count=10
custom_categories_count=3
records_per_day=3
records_days_ago=365
tmp_dir="/home/kamabizbazti/tmp/"
get_version_url="http://""${application_server_host}"":""${application_server_port}""/general/getVersion"
timeout=500
valid_code=200
version_validated=false

function set_context {
    tomcat_context=/${application_target_filename%.*}
}

function clean_db {
    echo "Clean database"
    mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -v -v -v -e "DELETE FROM "${database}".record;
    DELETE FROM "${database}".category;
    DELETE FROM "${database}".user_authorities;
    DELETE FROM "${database}".user;
    DELETE FROM "${database}".selection;
    DELETE FROM "${database}".language;
    DELETE FROM "${database}".currency;
    DELETE FROM "${database}".authority;"
    records_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM record")
    categories_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM category")
    user_authorities_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM user_authorities")
    user_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM user")
    selection_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM selection")
    language_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM language")
    currency_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM currency")
    authority_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM authority")
    if [ "${records_count}" -ne 0 ] || [ "${categories_count}" -ne 0 ] || [ "${user_authorities_count}" -ne 0 ] || [ "${user_count}" -ne 0 ] || [ "${selection_count}" -ne 0 ] || [ "${language_count}" -ne 0 ] || [ "${currency_count}" -ne 0 ] || [ "${authority_count}" -ne 0 ];
        then
            echo "Clean database failed"
            echo "Roll back"
            restore_database
            exit 1
    fi
}

function drop_db {
    echo "Drop database"
    mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -v -v -v -e "SET FOREIGN_KEY_CHECKS = 0;
    DROP TABLE IF EXISTS "${database}".record;
    DROP TABLE IF EXISTS "${database}".category;
    DROP TABLE IF EXISTS "${database}".user_authorities;
    DROP TABLE IF EXISTS "${database}".user;
    DROP TABLE IF EXISTS "${database}".selection;
    DROP TABLE IF EXISTS "${database}".language;
    DROP TABLE IF EXISTS "${database}".currency;
    DROP TABLE IF EXISTS "${database}".authority;
    DROP TABLE IF EXISTS "${database}".activation_token;
    DROP TABLE IF EXISTS "${database}".verification_token;
    SET FOREIGN_KEY_CHECKS = 1"
    tables_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='"${database}"'")
    if [ "${tables_count}" -ne 0 ];
        then
            echo "Drop database failed"
            echo "Roll back"
            restore_database
            exit 1
    fi
}

function waiting {
time=0
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

function is_running {
code=$(curl -m 2 -s -o /dev/null -I -w "%{http_code}" -X GET "${get_version_url}")
        if [ "${code}" -eq "${valid_code}" ]
            then
            echo "Application is running"
            return 0
            else
            echo "Application is NOT running"
            return 1
        fi
}

function load_data {
    echo "Load test data"
    categories_count_before=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM category")
    mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -v -v -v -e "# Ver. Buggeez 1.1
    DELIMITER //
    DROP PROCEDURE IF EXISTS "${database}".generator//
    CREATE DEFINER = "${database}"@localhost PROCEDURE generator(IN usersCount INT, IN customCategoriesCount INT, IN recordsPerDay INT, IN recordsDaysAgo INT)
      BEGIN
        DECLARE exit_loop BOOLEAN;
        DECLARE f_name VARCHAR(50);
        DECLARE l_name VARCHAR(50);
        DECLARE e_mail VARCHAR(100);
        DECLARE uid BIGINT;
        DECLARE cat_type VARCHAR(20);
        DECLARE cat_id BIGINT;
        DECLARE cat_count INT;
        DECLARE rec_per_day_count INT;
        DECLARE rec_days_ago_count INT;
        DECLARE time_stamp BIGINT;
        DECLARE cur CURSOR FOR SELECT * FROM generator_names LIMIT usersCount;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;
        OPEN cur;
        names_loop: LOOP
          FETCH cur INTO f_name, l_name, e_mail;
          IF exit_loop
          THEN
            CLOSE cur;
            LEAVE names_loop;
          END IF;
          SET cat_count = 1;
          SET rec_days_ago_count = 1;
          SET time_stamp = (SELECT UNIX_TIMESTAMP()) * 1000;
          INSERT INTO "${database}".user (id, creation_date, enabled, is_activated, status, last_password_reset_date, name, password, start_day, username, uuid, currency_code, language_code) VALUES (NULL, CURRENT_TIME(), b'1', b'1', 'ACTIVE', CURRENT_TIME(),CONCAT(f_name, ' ', l_name), '$2a$10$hDxjZ8W1R/al7Pik07ilt.Nvxvs6C7kDG7vtobmUDH5n/etxr587C', '1',e_mail, e_mail, '3', '1');
          SET uid = (SELECT LAST_INSERT_ID());
          INSERT INTO "${database}".user_authorities (users_id, authorities_id) VALUES (uid, 2);
          WHILE cat_count <= customCategoriesCount DO
            INSERT INTO "${database}".category (dtype, category_id, name, type, u_id, user_id)
            VALUES ('CustomCategory', NULL, CONCAT(f_name, ' ', cat_count), 'CUSTOM', uid, uid);
            SET cat_count = cat_count + 1;
          END WHILE;
          DROP TABLE IF EXISTS user_categories;
          CREATE TEMPORARY TABLE IF NOT EXISTS "${database}".user_categories AS (SELECT type, category_id FROM "${database}".category WHERE user_id = uid OR type = 'GENERAL');
          WHILE rec_days_ago_count <= recordsDaysAgo DO
            WHILE rec_per_day_count <= recordsPerDay DO
              SELECT type, category_id FROM "${database}".user_categories ORDER BY RAND() LIMIT 1 INTO cat_type, cat_id;
              INSERT INTO "${database}".record (record_id, amount, category_type, comment, date, category_id, id) VALUES (NULL, ROUND((RAND() * (100-1))+1), cat_type, NULL, time_stamp, cat_id, uid);
              SET rec_per_day_count = rec_per_day_count + 1;
            END WHILE;
            SET time_stamp = time_stamp - 86400000;
            SET rec_per_day_count = 1;
            SET rec_days_ago_count = rec_days_ago_count + 1;
          END WHILE;
        END LOOP names_loop;
        DROP TABLE IF EXISTS user_categories;
      END//

    DELIMITER ;
    CREATE TEMPORARY TABLE IF NOT EXISTS "${database}".generator_names (fname VARCHAR(50), sname VARCHAR(50), email VARCHAR(100));
    TRUNCATE TABLE generator_names;
    LOAD DATA LOCAL INFILE '"${names_database}"' INTO TABLE generator_names
    FIELDS TERMINATED BY ','
    ENCLOSED BY '\"'
    LINES TERMINATED BY '\r\n' (fname, sname, email);
    CALL generator("${users_count}", "${custom_categories_count}", "${records_per_day}", "${records_days_ago}");
    DROP TABLE IF EXISTS "${database}".generator_names;
    DROP PROCEDURE IF EXISTS generator"
    records_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM record")
    categories_count_after=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM category")
    user_authorities_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM user_authorities")
    user_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM user")
    selection_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM selection")
    language_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM language")
    currency_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM currency")
    authority_count=$(mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" -se "SELECT COUNT(*) FROM authority")
    echo "Records count:			" "${records_count}"
    echo "Categories count before:	" "${categories_count_before}"
    echo "Categories count afrer:	" "${categories_count_after}"
    echo "User authorities count:	" "${user_authorities_count}"
    echo "Users count:				" "${user_count}"
    echo "Selection count:			" "${selection_count}"
    echo "Languages count:			" "${language_count}"
    echo "Currency count:			" "${currency_count}"
    echo "Authorities count:		" "${authority_count}"
    if [ "${records_count}" -gt 0 ] && [ "${categories_count_after}" -gt "${categories_count_before}" ] && [ "${user_authorities_count}" -gt 0 ] && [ "${user_count}" -gt 0 ] && [ "${selection_count}" -gt 0 ] && [ "${language_count}" -gt 0 ] && [ "${currency_count}" -gt 0 ] && [ "${authority_count}" -gt 0 ];
        then
            echo "Load data completed"
        else
            echo "Data loading failed"
            echo "Roll back"
            restore_database
            exit 1
    fi
}

#Parameter $1 is replace properties file
function deploy() {
echo $1
if [ $1 = true ]
    then
        echo "Deploy from artifacts"
        cd "${workspace}"
        jar -xvf "${build_package_name}"
        rm -f WEB-INF/classes/application.properties
        cp "${config_file}" WEB-INF/classes/application.properties
        jar -uvf "${build_package_name}" WEB-INF/classes/application.properties
        rm -rf META-INF/ org/ WEB-INF/
        mv "${build_package_name}" "${tmp_dir}""${application_target_filename}"
fi
    echo 'curl --upload-file '"${tmp_dir}""${application_target_filename}"' --user '"${tomcat_manager_username}"':'"${tomcat_manager_password}"' http://'"${application_server_host}"':'"${application_server_port}"'/manager/text/deploy?path='"${tomcat_context}"'&update=true'
    if curl -v --upload-file "${tmp_dir}""${application_target_filename}" --user "${tomcat_manager_username}":"${tomcat_manager_password}" http://"${application_server_host}":"${application_server_port}"/manager/text/deploy?path="${tomcat_context}"&update=true;
    then
        echo "Container deploy success"
        return 0
    else
        echo "Container deploy fail"
        return 1
    fi
}

function undeploy {
echo 'curl --user '"${tomcat_manager_username}"':'"${tomcat_manager_password}"' http://'"${application_server_host}"':'"${application_server_port}"'/manager/text/undeploy?path='"${tomcat_context}"
    if curl -v --user "${tomcat_manager_username}":"${tomcat_manager_password}" http://"${application_server_host}":"${application_server_port}"/manager/text/undeploy?path="${tomcat_context}";
    then
        echo "Container undeploy success"
        return 0
    else
        echo "Container undeploy fail"
        return 1
    fi
}

function restart_container {
echo 'curl --user '"${tomcat_manager_username}"':'"${tomcat_manager_password}"' http://'"${application_server_host}"':'"${application_server_port}"'/manager/text/reload?path='"${tomcat_context}"
    if curl -v --user "${tomcat_manager_username}":"${tomcat_manager_password}" http://"${application_server_host}":"${application_server_port}"/manager/text/reload?path="${tomcat_context}";
    then
        echo "Container restart success"
        return 0
    else
        echo "Container restart fail"
        return 1
    fi
}

function validate_version {
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
    #Undeploy and delete current build
    undeploy

    # Restore DB from dump
    echo "Restore DB from dump"
    restore_database

    # Restore old build from backup
    echo "Restore old build from backup"
    deploy false
    sleep 5
    echo "Waiting for application startup:"
    waiting

    echo "Deployment failed, old version was recovered"
    exit 1
}

function restore_database {
    echo "Restore DB from dump"
    if mysql -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" < "${tmp_dir}""${database}".sql;
    then
        echo "Restore database success"
        return 0
    else
        echo "Restore database fail"
        exit 1
    fi
}

function backup_war {
if [ "${is_upgrade}" = true ]
    then
    if sshpass -p "${application_server_password}" scp -o StrictHostKeyChecking=no "${application_server_username}"@"${application_server_host}":"${application_webapp_path}""${application_target_filename}" "${tmp_dir}""${application_target_filename}";
    then
        echo "War file backup success"
        return 0
    else
        echo "War file backup fail"
        exit 1
    fi
else
    echo "No need to back up war file"
    return 0
fi
}

function backup_database {
if [ "${is_upgrade}" = true ]
    then
    if mysqldump -h "${database_host}" -u "${database_username}" -p"${database_password}" "${database}" > "${tmp_dir}""${database}".sql;
    then
        echo "Database backup success"
        return 0
    else
        echo "Database backup fail"
        exit 1
    fi
else
    echo "No need to back up database"
    return 0
fi
}

set_context

#Is running
if [ "${is_upgrade}" = true ]
    then
        if is_running; then
        echo "Upgrade flow is running"
        else
        echo "Upgrade mode is selected, but the application is not running. Cancel deploy process"
        exit 1
        fi
fi

# Backup previous build
echo "Backup previous war file"
backup_war

# Take DB dump
echo "Take DB dump"
backup_database

# Clear database
echo "Clear database"
if [ "${is_upgrade}" = true ]
    then
        echo "Database upgrade"
        drop_db
        echo "Restart application"
        restart_container
        sleep 5
        echo "Waiting for application startup:"
        waiting
        echo "Load demo data"
        load_data
else
    drop_db
fi

# Undeploy build
echo "Undeploy current build"
undeploy

# Deploying new build
echo "Deploying new build"
deploy true
echo "Waiting for application startup:"
waiting

# Validate new version
echo "Validate new version"
validate_version

# Load test data
if [ "${load_test_data}" = true ] && [ "${is_upgrade}" = false ]
    then
    echo "Load test data without upgrade test"
    load_data
fi

# Roll back if failed
if [ "${version_validated}" = false ]
    then
    echo "Roll back"
    roll_back
    exit 1
fi