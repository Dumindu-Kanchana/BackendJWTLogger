# BackendJWTLogger
simple http springboot app for logging a jwt which comes in the request header. 

To run the application,
1. build the project
2. copy the jar file and the files in resource folder to a single directory.
3. configure the HOME_LOG and the fileNamePattern in the logback.xml.
4. run the jar file with the command. <br />
```sh
            java -jar BackendJWTLogger-1.0.0.jar
```
Or you can specify the application.properties file location with the following command.
   
```sh
java -jar BackendJWTLogger-1.0.0.jar --spring.config.location=/path/application.properties
```
