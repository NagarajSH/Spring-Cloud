Spring Cloud Config
-------------------
Spring cloud configuration files are typically YML, properties files. It can serve any text files. Typical file names generally in the format  {application_name}-{profile}, where profile is the enviornment name. Default profile name is native. Spring cloud config returns all matched files.

why spring cloud config
-----------------------
Applications are not just about code. Usually they need connection to resources like databases, messaging queues, email servers, web services etc. 

It is always suggested to externalize configuration to change the runtime behaviour of the application. Spring cloud is useful in this situations.

Different configuration options
-------------------------------
1)package configuration files with the application. --> It requires rebuild,restart if changed.
2)configuration files in file system --> Not available in cloud.
3)use environment variables --> These are done differently on different os platforms. Also, tough to manage large no of individual variables to manage.
4)cloud vendor specific solution --> Tightly coupled to the vendor.

The role of configuration in micro services
-------------------------------------------
1)Configuration removes settings from compiled code. Configurations like connection strings,logging settings etc are externalized so that the code is easily portable between different environments.
2)Configurations can be used to change runtime behaviour. For ex, changing logging levels for different environments for debugging. This is only possible if configuration is present externally otherwise you may need to redeploy the entire application.
3)Configuration provides consistency across elastic services.i.e. It is very common to scale up/scale down the service instances. While doing this, it is required to have consistency because when scaling up an instance, we know that it shares some common configuration.

Problems with local configuration
---------------------------------
1)Local configuration files may fall out of sync. Packaging an application with configuration itself can lead to security issues. Also, a single change can cause the continuous integration process run again and can result in a redeployment.
2)For large no of microservices, it's tough to manage manually.
3)No history of changes with env variables. For ex, if someone changes the configuration in production, it is tough to track what were the previous values.
4)There can be challenges with sensitive information. You may accidentally expose the production configuration.
5)Inconsistent usage across teams.

Desired solution for configuration
----------------------------------
1)platform/cloud independent solution
2)centralized
3)dynamic --> should be able to update while the application is running
4)controllable --> like source control management choices
5)passive --> services should do most of the work by self registering.(useful in systems with large no of microservices.)

Creating a config server
------------------------
Spring cloud config is http access to git or file based configurations. Creating a config server has the following steps.
1)Choose a config source(git or file based)
2)Add config files(different formats are supported)
3)Build the spring project(spring boot is the easiest way to do this)
4)secure the configurations

Note: Clients connect to config server over http. So, client applications can be in any technology. Also, Config server should be highly available. So, there should be more than one instance of it should be running and should be load balanced.

Config Server for local files
-----------------------------
The easiest way to do is by following the steps below.
1)create a spring starter project
2)Annotate the main class(@EnbaleConfigServer)
3)Set the application properties(properties of config server app like port,profile etc)
4)Add local configuration files(properties files that you want to expose)

Working with Config Server URIs
-------------------------------
The configuration file in git should be of the format : <spring.application.name>-<spring.profiles.active>.yml

Note: yml takes precedence over properties file. Yml file can take multiple properties in a single file.

Instaead of having s1rates-dev.properties and s1rates-prod.properties, we can write them in a single file like this.

	#s1rates.yml
	---
	spring:
	  profiles: dev
	property: value

	---
	spring:
	  profiles: prod
	property: value  

Client applications access configuration through URI pattern http://<server>:<port>/<spring.application.name>/<spring.profiles.active>[/{label}], where label is optional.

Note: client applications should have the properties spring.application.name and spring.cloud.config.uri in bootstrap.properties file.

Note: client applications access the configuration using the above URL automatically on startup.

For ex, lets take the following folder structure of a git configuration repository where rates is the root directory.
	rates |
	      |--> application.properties
		  |--> station1 |
		  |			    |-->s1rates-dev.properties
		  |			    |-->s1rates-qa.properties	
		  |			    |-->s1rates.properties
		  |
		  |--> station2 |
						|-->s2rates-dev.properties
						|-->s2rates.properties
						
Sample application.yml(or properties) file of Config server 
------------------------------------------------------------
#Port on which config server is running
server:
  port: 8888
#uri of git configuration   
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/tulasidamarla/config-properties
          #use the below two properties for private access
          #username: tulasidamarla 
          #password: krishna@418
          
          #search for subfolders name starts with station
          search-paths:
            - 'station*'
          repos:
            #alternate repo name 'perf'
            perf:
              #url pattern For ex: localhost:8888/s1rates/perf
              pattern:
                -"*/perf"
              uri: https://github.com/tulasidamarla/config-properties-perf
              search-paths:
                -'station*'

Note: repos property is used to configure alternate repo(For ex testing, performance). you should configure alternate repo to be invoked with different uri pattern(like */perf).

Examples
--------
/s1rates/default returns application.properties,station1/s1rates.properties because application.properties is default one for all and s1rates.properties is default for s1 rates. 
/s1rates/qa returns application.properties,station1/s1rates.properties and station1/s1rates-qa.properties 
/s1rates-prod returns application.properties, station1/s1rates.properties 
/s3rates/dev returns application.properties because application.properties file is default
/s3rates returns 404 because profile names is missing.

*****
Note: Spring cloud config server uses EnvironmentRepository. There are two implementations available for Git and file systems. If you want to use any other source, implement EnvironmentRepository interface. This interface has one method "findOne".

Consuming Configurations from client Applications
-------------------------------------------------
Spring applications use Config servers as property source. They load values based on app name, spring profile, label etc.
app name comes spring.cloud.config.name or spring.application.name property.
profile comes from spring.cloud.config.env or spring.profiles.active property.
label comes from spring.cloud.config.label property.

How properties work in client applications
------------------------------------------
Spring apps have an Environment object. Environment object contains multiple property sources. These property sources typically are env variables,system properties, jndi , properties files etc.

Spring cloud config client library adds another property source by connecting to server over http with the url format of the config server as mentioned above.

As a result of this, server properties become part of client's application.

what if config server is down?
Ans: Typically it should run on several machines, so downtime should not be an issue.

Client applications should handle the case of missing server with the following setting.

	spring.cloud.config.failFast = true (default is false)

Note: With the above setting, client applications won't run without server running. 

Note: By default config server properties takes precedence over local settings on the client applications. The better approach is to provide local fallback settings incase of server failure.

Security to Configurations
--------------------------
Adding basic authentication and testing it has the following steps.
1)Add pom dependency spring-boot-starter-security
2)Test the project to get authentication error
3)Add Basic auth credentials and call api with valid credentials
4)update client apps with credentials

Note:Adding spring-boot-starter-security pom dependency automatically creates basic http auth. you can notice the default password set by spring on console like this.
	Using default security password: 013f643c-8511-416d-839e-a61722b3bb83
Now accessing config server with /s1rates/default gives an error like this.
	{
	  "timestamp": 1493058501682,
	  "status": 401,
	  "error": "Unauthorized",
	  "message": "Full authentication is required to access this resource",
	  "path": "/s1rates/default"
	}	

*****	
Note:To access the config server with auto generated security, you should set the Basic Auth Header with username as 'user' and password as generated on the console.

To disable Basic Auth security, change add the below property in application.properties file.

	security:
	  basic:
		enabled: false 

To define your own username and password, do the following.
	
	security:
	  basic:
		enabled: true
	  user:
		name: user1
		password: passwd123

Note: The client applications can't access configuration server. For client applications to work, do the following changes in bootstrap.properties file of the client application.
	
	spring.cloud.config.username=user1
	spring.cloud.config.password=passwd123

*****
Note: bootstrap.properties file is the first configuration file that is loaded before anything else for the application to bootstrap. For ex, if you want to change the profile of the client app to qa, then spring.profiles.active=qa should be added to bootstrap.properties not to application.properties file, because by the time application.properties is loaded, profile is already choosen.

Encrypting and Decrypting Configurations
----------------------------------------
Encryption is useful, if someone accidentally got access to the URL, all the sensitive information like connection strings, usernames, passwords etc is not human readable.
Also, if property values are properly encrypted, your git repository can be public it is not necessarily to be private.

Encryption and Decryption can be done two ways. Symmetric and Assymetric keys.
Symmetric option shares a key where as assymetric option shares a key-pair.
Assymetric option is more robust and secure.

Symmetric option is easy to set. All we need to do is to set encrypt.key property to some secret value in properties file. Ideally it is an environment variable rather than a property in properties file.

Assymetric key is set as a pem encoded text value using encrypt.key or using a key store  
which is handy bcoz it comes with jdk. So, Assymetric options are more secure because they register some keys.

Encryption end points
---------------------
Spring cloud config server automatically provides two end points /encrypt and /decrypt assuming that you have to actually securing the end points. These end points help in encryption and decryption at the configuration server.

Note: Instead of sending the unencrypted values over the wire, you can do encryption and decryption at the client to achieve better security.

Encryption and decryption steps
-------------------------------
1)Download full strenth JCE(The Java Cryptography Extension (JCE) is an officially released Standard Extension to the Java Platform and part of Java Cryptography Architecture. JCE provides a framework and implementation for encryption, key generation and key agreement, and Message Authentication Code (MAC) algorithms)
2)Add key to bootstrap.properties file.
3)Generate encrypted value and add to the properties file.
4)Retrieve configuration via API
5)Test client app with server side decrypted value
6)update server to require client side decryption
7)change client to decrypt.

Steps for config server encryption 
------------------------------------
1)Download jce from http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html and extract the zip file jce_policy-8.zip.
2)copy the local_policy.jar and US_export_policy.jar from extracted zip directory to jre_install_location/lib/security. Take backup of those two jar files in lib/security  directory if needed.
3)Add encrypt.key property to bootstrap.properties of your config server app. 
4)Use postman or rest-client to send a post request to /encrypt endpoint (Remember to set username and password in Authorization header) and send the below in request body.
	connString=server123;user=root;password=passwd123
I got the below response in my machine.
af65ff719716c232518cb14017df96fb10de2af655874ef72b96882b73f69e5b3c6915271fe7ddced739b3c75822e09b58ba1e7eb355b3848a091898090b7135a852306d88946da4f64b083cf5289cf9

Note: you can verify the above response with /decrypt end point with above encrypted request to retrieve your original request body.
5)Add the encrypted value in git like this.
connstring={cipher}af65ff719716c232518cb14017df96fb10de2af655874ef72b96882b73f69e5b3c6915271fe7ddced739b3c75822e09b58ba1e7eb355b3848a091898090b7135a852306d88946da4f64b083cf5289cf9

Steps for client side encryption/decryption
-------------------------------------------
1)For client side encryption first disable server side encryption/decryption by adding the following property.
	spring.cloud.config.server.encrypt.enabled=false

2)Add encrypt.key to the config client app's bootstrap properties file and restart the client.

Refreshing Configurations
-------------------------
If a property is changed in git, client application can get those changes without restarting. The following steps are required for this.
1)Annotate the client Controller class with RefreshScope
2)Hit the client app url /refresh to take the updated values. This is a POST url not GET.
