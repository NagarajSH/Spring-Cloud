1)Get rabbitmq installer for windows and install it.<br>
2)Add spring-cloud-starter-bus-amqp and actuator dependencies in config-server and also in all microservices.<br>
3)Move all the microservices words property into git. Make sure that name of the file matches the pattern {spring.application.name}-{profile}.properties. For ex, service-noun-primary.properties<br>
4)All the properties name should be prefixed with some string. For ex: noun.words=bat,book<br>
5)In the controller classes of all microservices, add the classlevel annotation @ConfigurationProperties. For ex:  @ConfigurationProperties(prefix="adjective")<br>
Note: That prefix should match the one defined in step 4.<br>
6)Add setters and getters for the property "words" in all controller classes.<br>
7)Start config server, discovery server, micro services and test the service-sentence.<br>
8)Modify some or all the properties in git, and make a post request to the uri /bus/refresh of config-server.<br>
9)test the service-sentence again to test if the changes are reflected.
