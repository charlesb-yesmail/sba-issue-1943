# Spring Boot Admin Issue 1943
This project is an attempt to provide a simple example of the issue described in issue
1943: https://github.com/codecentric/spring-boot-admin/issues/1943

It comprises 3 separate applications, which mimics the configuration in which the issue was observed:

* standalone: a Spring Boot application that is packaged as a fat JAR and run from the command line
* webapp1: a Spring Boot MVC application packaged as a WAR and deployed to a Tomcat server
* webapp2: a Spring Boot MVC application packaged as a WAR and deployed to the same Tomcat server as webapp1

## Tomcat Server configuration

Tomcat 9.0.38 (or higher)

Add the following to the ```Engine``` section of Tomcat's ```server.xml``` file:

```xml
    <Engine name="Cataline" defaultHost="localhost">
        <!--
        default settings here
        -->
        <!-- Additional configuration for the vhosts used by these applications -->
        <Host name="webapp1.com" appBase="webapps" unpackWARS="true" autoDeploy="true">
            <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                   prefix="webapp1_access_log" suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
		    <Context path="" docBase="webapp1" debug="0" reloadable="true" />
		    <Alias>webapp1.com</Alias>
	    </Host>
        <Host name="webapp2.com" appBase="webapps" unpackWARS="true" autoDeploy="true">
            <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" 
                   prefix="webapp1_access_log" suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
		    <Context path="" docBase="webapp2" debug="0" reloadable="true" />
		    <Alias>webapp2.com</Alias>
	    </Host>
    </Engine>
```
Add the following to Tomcat's ```setenv.sh``` file:
```shell
# Spring Boot Admin settings
# Set Spring Boot Admin properties
export SPRING_SECURITY_USER_NAME="username"
export SPRING_SECURITY_USER_PASSWORD="password"
export SB_ADMIN_USER=${SPRING_SECURITY_USER_NAME}
export SB_ADMIN_PASSWORD=${SPRING_SECURITY_USER_PASSWORD}
export CATALINA_OPTS="$CATALINA_OPTS -Dspring.boot.admin.client.username=$SPRING_SECURITY_USER_NAME -Dspring.boot.admin.client.password=$SPRING_SECURITY_USER_PASSWORD -Dspring.boot.admin.client.instance.metadata.user.name=$SPRING_SECURITY_USER_NAME -Dspring.boot.admin.client.instance.metadata.user.password=$SPRING_SECURITY_USER_PASSWORD -Dspring.boot.admin.username=$SB_ADMIN_USER -Dspring.boot.admin.password=$SB_ADMIN_PASSWORD -Dmanagement.endpoint.health.roles=ACTUATOR -Dspring.boot.admin.client.url=http://localhost:9060 -Dspring.boot.admin.client.instance.service-base-url=http://localhost:8081"
```
Add the following aliases to the computer's hosts file:

```
127.0.0.1   webapp1.com
127.0.0.1   webapp2.com
```
Build the WAR files for ```webapp1``` and ```webapp2``` and deploy them to the Tomcat server.

###  Web App 1

Submit a request to ```http://webapp1.com:8081/p/hello?name={your name}}```

The response is a page with the text ```Hello, {your name}.```

### Web App 2

Submit a request to ```http://webapp2.com:8081/index```

The response is an image of a barcode

## Standalone app

Build the application's JAR file and copy it to a directory alongside the ```set_env.sh```
and ```start.sh``` files.  To start the application simply run the ```start.sh``` file.

To make a request use ```curl``` as follows:

```shell
curl -X GET http://localhost:8080/v1/services/hello/{your name}
```

The response is ```Hello, {your name}```.

## SpringBoot Admin

Create a Spring Boot Admin application configured with Spring Security as described in the documentation:

https://codecentric.github.io/spring-boot-admin/current/#securing-spring-boot-admin

Configure the ```SPRING_SECURITY_USER_NAME``` to be ```username``` and the
```SPRING_SECURITY_USER_PASSWORD``` to be ```password```.
```SPRING_BOOT_ADMIN_CLIENT_INSTANCE_METADATA_USER_NAME``` and ```SPRING_BOOT_ADMIN_CLIENT_INSTANCE_METADATA_USER_PASSWORD```
should be set to the same values.

Run the Spring Boot Admin service.

Log into Spring Boot Admin using the username ```username``` and password ```password```.

You should see the Standalone application registered as expected.

For Web Apps 1 and 2, both will be registered, but you will them exhibit the behaviour described in the original bug
description.

I think the issue is related to the use of Tomcat vhosts.  When I first set up this demo project I did not configure the
Tomcat vhosts and I did not observe the behaviour.  When I subsequently added the additional vhost configuration then the
behaviour started.
