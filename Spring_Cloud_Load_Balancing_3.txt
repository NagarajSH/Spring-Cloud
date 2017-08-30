Load Balancer
-------------
Traditionally load balancer are server side component used to distribute incoming traffic among several servers using some algorithm like round-robin etc.

Load balancers can be either softwares like Apache, Nginx, HA proxy etc or often there are hardware loadbalancer applications like F5, NSX, BigIP etc for better reliability and faster performance.

The above load balancers are server side load balancers. spring cloud ribbon provides us the client side load balancing. It decides which server to call. It is based on some criteria like round-robin or depends which server responding faster etc.

Note: server can still have it's own load balancer.

why client side load-balancing?
Ans:
1)There is a limit to the number of requests the load balancer itself can handle.
2)There is an extra cost related to operating a dedicated load balancer, which can run into tens of thousands of dollars. The backup load balancer generally does nothing other than wait for the primary to fail.

Note: Some servers may be slower than others, in that case client side load-balancing will help. Also, some servers may be located in far away regions.

Spring Cloud Netflix Ribbon
---------------------------
It is a client side load balancer and is well tested by netflix. It automatically integrates with Eureka. It also built for failure resilency(hystrix).

Note: Ribbon also supports caching and batching. caching saves calls to servers if it saves response for similar calls. Batching is used to group several requests into one call.
 
*** 
Spring cloud ribbon decides on what list of possible servers are available for a given client.
These list of servers can be populated static way via properties file or spring cloud config.
These can also be populated using service discovery.

Note: Spring cloud by default uses eureka, when found on classpath.

Sample static properties file for list of servers configuration for ribbon.
	
	stores.ribbon.listOfServers=store1.com,store2.com
	products.ribbon.listOfServers=products1.com,products2.com
	
Note: stores,products are serviceId's of the servers.

Filtered list of servers
------------------------
Given a list of servers, ribbon supports the criteria by which we can limit the subset of servers. Spring cloud, by default filter servers which are in the same zone.

Ping
----
ribbon uses two strategies.
1)Ping is used to test if the server is up or down.
2)Spring cloud by default delegate to eureka to check if the server is up or down.	

Load balancer
-------------
The load balancer is the actual component that routes the calls to the servers in the filtered list. It is usually defer to a rule component to make actual decisions. Spring cloud by default uses ZoneAwareLoadBalancer.

***
Note: Rule is the single module of intelligence that makes the decision on whether to call or not.
Spring cloud's default is ZoneAvoidanceRule.

Using Ribbon with spring cloud need the following dependency:
	
	<dependencies>
		<dependency>
			<groupid>org.springframework.cloud</groupid>
			<artifactid>spring-cloud-starter-ribbon</artifactid>
		</dependency>
	</dependencies>

Note: There is no server side depency like spring cloud config and eureka.

Loadbalancer can be accessed directly like this:

	public class MyClass{
		@Autowired
		LoadBalancerClient loadbalancer;
		
		public void doStuff(){
			ServiceInstance instance = loadbalancer.choose("subject");
			URI uri = URI.create(String.format("http://%s:%s",instance.getHost(),instance.getPort());
			//TODO: write logic
		}
	}

Note: subject is the clientId(or serviceId) of the service.

Customizing
-----------
Each client will get it's own list of servers for load balancing. So, client can customize this depends on the need. To overwrite the defaults, write a seperate config with replacement bean. For ex,
	
	@Configuration
	@RibbonClient(name="subject", configuration="SubjectConfig.class")
	public class MainConfig{}
	
Note: Client needs to write the SubjectConfig class like this.
	
	@Configuration
	public class SubjectConfig{
		@Bean
		public IPing ribbonPing(IClientConfig config){
			return new PingUrl();
		}
	}
