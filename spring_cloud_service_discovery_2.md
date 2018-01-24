Service Discovery
-----------------
Service Discovery is about your client application discovering other clients and has itself been discovered by others.

Need for service discovery
--------------------------
1)Microservices architecture results in large no of inter-service calls, so it is very challenging to configure.
2)If one application wants to easily find all other dependencies, manual configuration is impractical.

Service Discovery provides  a single lookup service, so that clients register themselves and discover other registered clients.

Note: There are various solutions available like Eureka, Consul, Etcd, Zookeeper, SmartStack etc.

Eureka
------
Eureka is part of spring cloud netflix. It is battle tested by netflix. i.e. it is currently used by netflix for production.

Eureka provides a lookup server. It registers all the clients. It is generally made highly available by running multiple copies. These multiple instances of Eureka, copies the state of registered services between them.

Client applications register with Eureka by providing metadata like host, port , health indicator URL etc.

Note: client services send heartbeats to Eureka. Eureka removes services if no heartbeats.

Making a Eureka Server
----------------------
Eureka server is a regular spring boot application with dependencies and @EnableEurekaServer annotation.

	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka-server</artifactId>
	</dependency>

	@SpringBootApplication
	@EnableEurekaServer
	public class Application{
		public static void main(String[] args){
			SpringApplication.run(Application.class,args);
		}
	}

Note: Typically, multiple Eureka servers should run simultaneously, otherwise you will get many warnings in the log.

Each Eureka server, should know the URL to the others for sharing the state and high availability. These URL's are provided by configuration server.

Multiple servers configuration
------------------------------
Some of the common configuration options for Eureka server as follows.

		eureka.client.registerWithEureka = false
		eureka.client.fetchRegistry = false
		eureka.client.serviceUrl.defaultZone = http://server:port/eureka,http://server:port/eureka

Note: The first two properties are not default. Setting to false, indicates that it is not for production use, it is just for local testing. In real production, they should be true. This setting eliminates warnings on the console.

Note: The last property above is a comma seperated list of other eureka server URL's. Typically they should be coming from configuration server.

Discovery Client
----------------
Registering with Eureka server needs the following.
1)Add the same eureka server dependency.
	
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-eureka</artifactId>
	</dependency>

2)Client application
	
	@SpringBootApplication
	@EnableDiscoveryClient
	public class Application{
	
	}

3)Add the below property in application.properties file.
	
	eureka.client.serviceUrl.defaultZone=http://server:port/eureka,http://server:port/eureka-2
	
Note: Annotation for client is @EnableDiscoveryClient, It is not specific to Eureka.

Locating other services
-----------------------
The above 3 steps to register with Eureka server, will automatically register with server. Client registers the application name, host and port. It uses default values from Environment object, you can override applicaiton name by specifying spring.application.name property.

Now the application can locate other services using the below code snippet.

	@Autowired
	DiscoveryClient client;
	
	public URI storeServiceUrl(){
		List<ServiceInstance> list = client.getInstances("STORES");
		if(list != null && list.size() > 0){
			return list.get(0).getUri();
		}
	}
	
Note: STORES is the Service	ID corresponding to other application's spring.applicaiton.name property.
*****
Note: Eureka server does not persist service registrations. It relies on client registrations, always up to date, always in memory.

Eureka or Config server
-----------------------
1)Config first bootstrap: Use config server to configure location of Eureka server. It means 
spring.cloud.config.uri should be configured in each app.

2)Eureka first bootstrap: Use Eureka to expose location of config server. It means config server is just another client. It requires spring.cloud.config.discovery.enabled = true and eureka.client.serviceUrl.defaultZone configured in each app.

Note: In Eureka first approach, client makes two network trips to obtain configuration.
Note: Config first bootstrap is the default approach and Eureka first bootstrap is the less common.

Note: If you want more information go through the following link:
https://spring.io/blog/2015/01/20/microservice-registration-and-discovery-with-spring-cloud-and-netflix-s-eureka



	
