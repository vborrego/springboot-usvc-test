#!/bin/sh
echo "Starting services"
java -jar eureka-server/target/eurekaserver-0.1.0.jar &
sleep 60
java -jar spring-cloud-gw-server/target/spring-cloud-gw-server-0.1.0.jar &
sleep 60
java -jar chuck-norris/target/chucknorris-0.1.0.jar &
sleep 90
echo "Try access http://localhost:8761/ for eureka"
echo "Try access http://localhost:8111/createtask for micro service to create tasks"