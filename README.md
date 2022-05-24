# SleepyService
simple http springboot app that can be used as a slow backend. 

To run the application,
1. build the project
2. copy the jar file and the files in resource folder to a single directory.
3. configure the HOME_LOG and the fileNamePattern in the logback.xml.
4. run the jar file with the command. <br />
```sh
            java -jar SleepyService-1.0.0.jar
```
Or you can specify the application.properties file location with the following command.
   
```sh
java -jar SleepyService-1.0.0.jar --spring.config.location=/path/application.properties
```
Sample curl command

```sh
curl "http://localhost:8080/sleepy-backend/{sleep-time-in-seconds}"
```
