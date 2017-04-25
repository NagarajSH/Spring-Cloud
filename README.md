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
