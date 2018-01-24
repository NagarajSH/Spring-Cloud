# Spring-Cloud

why micro services architecture?<br>
Ans:<br>
1)Desire for faster changes:<br>
In a monolithic application change cycles are tied together and releases happen probably every six months/an year or may be at the end of the project. Modern business needs require faster changes and it is not possible to do that in a monolithic system.<br>
2)Need for greater availability:<br>
All the codebase is not tied to one giant application, there is greater availability. The idea of micro services is cloud computing as a whole.  you can't prevent failure with bigger and better hardware. Mean time between failure doesn't matter as much compared to the time it takes to recover. It is easy to recover a micro service than a monolith.<br>
3)Motivation for fine grained scaling:<br>
In monolith we have a few scaling factors like db,messaging tier, app tier etc. In micro services we can scale the ones which are needed.<br>
4)compatible with devops mindset:<br>
Microservices are a very good fit if we want deliver the code continously through automation.

Core characteristics of micro services<br>
--------------------------------------<br>
1)Components exposed as services<br>
2)Tied to a specific domain<br>
3)Loosely coupled<br>
4)Build to tolerate failure<br>
5)Delivered continously via automation<br>
6)Built and run by individual teams<br>

Martin Fowler about common characteristics of micro services

Componentization, the ability to replace parts of a system, comparing with stereo components where each piece can be replaced independently from the others.<br>
Organisation around business capabilities instead of around technology.<br>
Smart endpoints and dumb pipes, explicitly avoiding the use of an Enterprise Service Bus (ESB).<br>
Decentralised data management with one database for each service instead of one database for a whole company.<br>
Infrastructure automation with continuous delivery being mandatory.<br>

Cloud native application characteristics:
-----------------------------------------

The Application is composed of multiple services: what looks like a single application to the end user, for example a Software-as-a-Service (SaaS) human resources application, or a streaming music service, is actually delivered by a set of co-operating services. Clients interact with the application as a whole; typically via a single API. However, internally the application is made up of multiple cooperating services, much like an object-oriented application is made up of multiple cooperating objects.

Each service is elastic: this means that each service can scale-up or scale-down independently of other services. Ideally the scaling is automatic, based on load or other defined triggers. Cloud computing costs are typically based on usage, and being able to dynamically manage scalability in a granular manner enables efficient use of the underlying resources.

Each service is resilient: this means that each service is highly-available and can survive infrastructure failures. This characteristic limits the failure domain, due to software bugs or hardware issues.

Each service is composable: this implies that the service is designed to allow it to be part of other applications. At the minimum, each Service has an Application Programming Interface (API) that is uniform and discoverable, and in addition can have well defined behaviors for registration, discovery, and request management.

Q/A: Should every app turned into a set of micro services?<br>
Ans: Not necissarily. Some apps that don't change much and stay in more steady state and don't deserve their own team and continous delivery then keep things as they are because keeping your effort doesn't make much difference.

Q/A: How do I find my service if URIs can change?<br>
Ans: In a monolith everything is present in a configuration database or a service catalog, so manually can look at it. That doesn't work  in this model when many things are changing frequently.

Spring Cloud<br>
------------<br>
Spring cloud is released in March 2015. It is a way to build common distributed system patterns using spring. It is optimized for spring apps(but not mandatory). It can run anywhere like PCF, kubernetes or Heroku etc. It includes lot of netflix oss technology because netflix is a leader in defining micro services. 

Note: Initially AWS did not have all the things that required for netflix to scale. Netflix has created lot of technologies and later collaberated with pivotal to develop spring-cloud.

List of Spring-cloud projects<br>
-----------------------------<br>
Spring Cloud Config --> Git-backed configuration server<br>
Spring Cloud Netflix --> Suite for service discover, routing, availability etc.<br>
Spring Cloud Consul --> Service discovery with Consul<br>
Spring Cloud security --> Simplify OAuth 2.0 flows<br>
Spring Cloud Sleuth --> Distributed tracing<br>
Spring Cloud Stream --> Message bus abstraction<br>
Spring Cloud Task --> Short-lived, single-task micro services<br>
Spring Cloud Dataflow --> Orchestration of data micro services<br>
Spring Cloud Zookeeper --> Service discovery and configuration with zookeeper<br>
Spring Cloud for AWS --> Exposes core AWS services to spring developers<br>
Spring Cloud Spinnaker --> Multi-cloud deployment<br>
Spring Cloud Contract --> Stubs for service contracts<br>

Spring boot<br>
-----------<br>
Spring boot is an opinionated runtime for spring. It follows convention over configuration.i.e. It mostly depends on Annotation code than large xml configuration files. The opinions can be overridden. It handles boiler plate setup. It has simple dependency management. Embeds app server in executable jar. It has built-in end points for health metrics.

Spring-cloud
------------
The goal of Spring cloud is to address the need of cloud based applications. cloud based applications are distributed applications running in an environment that is best characterized as volatile. There are some common patterns that are needed to address the reality of this platform. These patterns include centralized configuration management,service registration and discovery,load balancing,circuit brakers,routing,gateway etc.

Note: Spring cloud projects are all based on spring boot for easier dependency management.
spring cloud applicationcontext startup process is modified, it is not done in normal way.

As mentioned about spring cloud is based on spring boot. The required parent dependency is needed as follows.

	<parent>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-parent</artifactId>
		<version>Angel.SR4</version>
	</parent>

Note: The version of parent changes very regularly. keep this up to date.
*****
Note: Depends on your organization project structure you may already have a parent dependency defined. In such cases where you can't change the parent pom, add the same configuration in dependencymanagement section of the project pom like this.

	<dependencymanagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-starter-parent</artifactId>
				<version>Angel.SR4</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencymanagement>
	
In the dependency section, based on the requirement you choose on of the dependencies as follows.

	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka</artifactId>
	</dependency>
	
###Spring-cloud-projects

[Spring Cloud Config](Spring_Cloud_Config_1.md)

[Spring Cloud Config](Spring_Cloud_Config_1.md)

