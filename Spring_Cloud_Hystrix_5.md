Circuit breakers
----------------
Having large no of microservices as dependencies can lead cascading failures. Circuit breakers protect against cascading failures.

Here are some stats of a Distributed system. As per Amazon ec2 SLA, they guarantee 99.95% uptime, then<br>
1)For a single app, downtime would be 22 mins per month<br>
2)30 interrelated services may cause 11 hours downtime per month(looks bad)<br>
3)100 interrelated services may cause 36 hours downtime per month

Circuit Breaker pattern
-----------------------
In general circuit breaker does the following<br>
1)It watches the circuit<br>
2)when failure occurs, it opens(disconnects) the circuit.<br>
3)when problem is resolved, manually close the breaker.

Note: By opening the cicuit, it basically avoids cascading failure.

Hystrix
-------
Hystrix is part of netflix OSS. Spring cloud provided light and easy to use wrapper on top of it.

1)It detects failure conditions and opens the network to disallow further calls. Hystrix default is 20 failures in 5 seconds.<br>
2)It identifies fallback. Fallback is the default behaviour incase of a service dependency failure.<br>
3)It automatically closes the network after interval. Hystrix default is 5 seconds.<br>

Note: Fallbacks can be chained.

Note: Compared to manual circuit breakers in real world, Hystrix has few advantages.<br>
1)Failure definition is flexible. It can consider failure when an exception is thrown or timeout perioud exceeded.<br>
2)Multiple fallback options available depends on failure type.<br>
3)Automatically re-closes the breaker.

Spring Cloud Hystrix setup
--------------------------
1)Add the dependency

	<dependencies>
		<dependency>
			<groupid>org.springframework.cloud</groupid>
			<artifactid>spring-cloud-starter-hystrix</artifactid>
		</dependency>
	</dependencies>

2)Enable Hystrix with in a configuration class.
	
	@SpringBootApplication
	@EnableHystrix
	public class Application{
	}

Hystrix example
---------------
Use the @HystrixCommand to wrap methods in a circuit breaker like this.

	@Component
	public class StoreIntegration{
		
		@HystrixCommand(fallbackMethod="defaultStores")
		public object getStores(Map<String, Object> parameters){
		}
		
		/*Write logic to return some default stuff*/
		public Object defaultStores(Map<String, Object> parameters){
			return "";
		}
	}

Customizing failure behaviour
-----------------------------
To customize failure behaviour use commandProperties and @HystrixProperty like below.
	
	@HystrixCommand(
		fallbackMethod = " defaultStores"
		commandProperties = {
		@HystrixProperty(name = "circutBreaker.errorThresholdPercentage", value="20"),
		@HystrixProperty(name = "circutBreaker.sleepWindowInMilliSeconds", value="1000")
		})
	public object getStores(Map<String, Object> parameters){}

Note: <br>
1)The first property configuration is to open circuit breaker if more than 20% failure in 10 seconds.<br>
2)The second parameter configuration is to close the breaker in 1 seconds instead of 5 seconds default.

Note: For more configurations please visit the Hystrix javanica Configuration. This is netflix's reference for java.

Command can be called in various ways like this.<br>
1)Synchronously --> call execute and block thread(default behaviour)<br>
2)Asynchronously--> Call in a seperate thread(queue), returning a future.<br>
3)Reactively --> Subscribe, get a listener(Observable)

Asynchronous command execution example
--------------------------------------
Method should return future.
	
	@HystrixCommand(...)
	public Future<Store> getstores(Map<String, Object> parameters){
		return new AsyncResult<Store>(){
			@Override
			public Store invoke(){
			
			}
		}
	}

Note: Method should be wrapped inside AsyncResult. Invocation of the method happens in a new thread managed by Hystrix.

Reactive command Execution example
----------------------------------
Method should return Observable.

	@HystrixCommand(...)
	public Observable<Store> getstores(Map<String, Object> parameters){
		return new ObservableResult<Store>(){
			@Override
			public Store invoke(){
			
			}
		}
	}

Note: Method should be wrapped inside ObservableResult. Note Observable is not java.lang.Observable. It is from RXJava.

Few important Hystrix Properties
--------------------------------
1)execution.isolation.thread.timeoutInMilliseconds --> waiting time(timeout) for success.<br>
2)circuitBreaker.requestVolumeThreshold --> # of requests in rolling time window(10 secs) that activate the circuitBreaker. (Not the no of errors)<br>
3)circuitBreaker.errorThresholdPercentage --> # of failed requests that will tip circuitBreaker.(default = 50%)<br>
4)metrics.rollingStats.timeInMilliseconds --> size of the rolling time window(default = 10 secs).<br>
5)circuitBreaker.sleepWindowInMilliSeconds --> timeout before closing the breaker(default = 5 secs)<br>
6)circuitBreaker.forceClosed --> Manually force the circuitBreaker closed.

Hystrix Dashboard
-----------------
Hystrix provides a built-in dashboard to check the status of the circuit breakers.

Hystrix Dashboard setup
-----------------------
1)Add the following dependency including actuator.

	<dependencies>
		<dependency>
			<groupid>org.springframework.cloud</groupid>
			<artifactid>spring-cloud-starter-hystrix-dashboard</artifactid>
		</dependency>
		<dependency>
			<groupid>org.springframework.cloud</groupid>
			<artifactid>spring-cloud-starter-actuator</artifactid>
		</dependency>		
	</dependencies>

2)Enable Hystrix Dashboard within a configuration class.
	
	@SpringBootApplication
	@EnableHystrix
	@EnableHystrixDashboard
	public class Application{}

Accessing Hystrix Dashboard
---------------------------
To access hystrix dashboard, use the url :  http://host:port/hystrix. <br>
For ex, http://localhost:64789/hystrix.	But, this is not the actual monitor. It has a text field, which will ask for the url of the stream you want to monitor.We generally, mention hystrix stream. i.e. we enter "http://localhost:64789/hystrix.stream" in the text field.

Note: All of the hystrix enabled endpoints will emit a stream called hystrix.stream.

Note: Each method that is annotated with @HystrixCommand appears on the hystrix dashboard.

Note: Monitoring large no of hystrix dashboard is not practical. 

Turbine is a single hystrix-dashboard that listens to and aggregates all other hystrix enabled streams. Turbine interacts with Eureka to find all of the client services.

