#!/bin/sh

#drop all tables
echo "Dropping database tables"
mysql -h localhost -u kamabizbazti -pkamabizbazti -e "SET FOREIGN_KEY_CHECKS = 0; DROP TABLE IF EXISTS budgeez.record; DROP TABLE IF EXISTS budgeez.purpose; DROP TABLE IF EXISTS budgeez.user_authority; DROP TABLE IF EXISTS budgeez.user; DROP TABLE IF EXISTS budgeez.selection; DROP TABLE IF EXISTS budgeez.language; DROP TABLE IF EXISTS budgeez.currency; DROP TABLE IF EXISTS budgeez.authority; SET FOREIGN_KEY_CHECKS = 1"

#sleep for 5 seconds
sleep 5

#deploy
echo "Deploying build"
sudo -i -u cubie /home/cubie/tests/deploy.sh

#create test users
echo "Create test users"
curl -X POST -H 'Content-Type: application/json' -d '{"email":"usertest@usertest.com","password":"123456","name":"Test User API"}' http://localhost:8080/kamabizbazti/signup
curl -X POST -H 'Content-Type: application/json' -d '{"email":"usertest2@usertest.com","password":"123456","name":"Test User API"}' http://localhost:8080/kamabizbazti/signup
echo

#generate purposes and records for users
echo "Generate purposes and record for test users"
curl -m 200 -X GET 'http://localhost:8080/budgeez/test/insertData?generateRecordsForAllUsers=true&generatePurposesForAllUsers=true&generateUsers=false'
echo

#run soapui project
echo "Running SoapUI sanity project"
sudo -i -u cubie /home/cubie/SoapUI-5.3.0/bin/testrunner.sh '/var/lib/jenkins/jobs/Kamabizbazti_1_0/workspace/target/test-classes/soapui_sanity.xml'

#clean database afrer sanity
echo "Clean database after sanity"
mysql -h localhost -u kamabizbazti -pkamabizbazti -e "DELETE FROM budgeez.record; DELETE FROM budgeez.purpose WHERE type ='CUSTOM'; DELETE FROM budgeez.user_authority; DELETE FROM budgeez.user;"
