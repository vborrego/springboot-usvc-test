# springboot-usvc-test
SpringBoot sets of micro services that determine the language which was used to
write a document sotred in UTF-8 enconding.

Uses Eureka as a Service Discovery and Spring Cloud Gateway as a API Gateway 
and Load Balancer.

The default paths for the dictionary files and the lucene worker are defined in 
the application.properties files.

# Build
Requires at least Java 8 and Maven 3.8.1. 
```
mvn clean install
```

# Run 
Execute run.sh 
```
sh run.sh
```

# Stop 
Execute listapps.sh and killapps.sh
```
sh listapps.sh
sh killapps.sh
```

# ToDo
* translate parts in portuguese to english
* add code coverage
* containerize the apps
* add also message listener to lucene worker instead of forking a new process 
for each identification task 
* add Twitter boostrap and change the app to an SPA (Single Page App)
