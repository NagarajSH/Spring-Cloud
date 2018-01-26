Accessing microservices via web
-------------------------------
Accessing over public internet is problematic due to following reasons.<br>
1)Internal API's should not be exposed.<br>
2)Security has to be implemented, so that client applications can authenticate with all the services.<br>
3)CORS required. Cross origin resource sharing is needed, because by default all browsers have security precaution that disallows javascript calls to the domains other than the one's from which they are originated.<br>
4)Multiple http calls are made by the client to get each word.

The Need for API gateway
------------------------
Different clients may have different needs.i.e. each client will not make calls to all services.For ex, mobile clients, desktop clients etc. We need an adapter to just do this. This is nothing but API gateway. API gateway provides simplified access for client. It does the following things.

1)Provides custom API.<br>
2)security implementation.<br>
3)Eliminates CORS issue.<br>
4)Reduces the no of http calls from the client.<br>
5)Other things like caching,filtering,protocol translation,transformation, link expansion etc can be done.

Spring cloud netflix Zuul
-------------------------
Zuul can be used for many API gateway needs. It is a jvm based router and load balancer.

Note: Router means that clients send request to Zuul and Zuul will send the request to the real server. This is also called proxying.

Zuul usage
----------
1)Create a spring boot application with spring-cloud-starter-zuul dependency.<br>
2)The above dependency will bring in all the needed dependencies including ribbon and hystrix.<br>
3)All we need is to enable the main class with @EnableZuulProxy

Note: Zuul registers with Eureka along with all other clients. It gets all clientId's and create routes to them. For ex, /service-subject routes to service-subject service.

Note: If zuul runs on port say, 8080, All we need is to call localhost:8080/subject, localhost:8080/verb etc to get access to different services. These request mappings are done by the in-built class org.springframework.cloud.netflix.zuul.web.ZuulController.

Zuul features
-------------
Zuul has lot of features with respect to routing.<br>
1)Services can be excluded by using property zuul.ignored-services<br>
2)Prefix can be added to the uri using property zuul.prefix=/api. Now url's become /api/noun etc.<br>
3)URL can be adjusted. For ex, /noun uri needs to be changed to /sentence-noun, then set the property zuul.routes.noun.path=/sentence-noun.

Note: By default all services are exposed using zuul. If you want to ignore use step1. For ex,
zuul.ignored-services = verb // This will black list verb service, hence cannot be accessed.

Note: Zuul is not an API Gateway. It is a tool using which you can form an API Gateway. For ex, Zuul has missed Caching, protocol translation, Resource expansion.

Caching possibilites
--------------------
There are two locations where caching can be applied. client cache and server cache.

If client is a web browser, then caching capability is built in. But, this won't com automatically. It depends on the server to send the correct http headers to tell the client what it can cache and for how long. For ex: expires,etags etc.

There is also possibility of caching between API Gateway and back end services.

Spring's caching Abstraction
----------------------------
It's very easy to use Spring's cache. <br>
1)Annotate methods using @Cacheable, which needs cache name and key.<br>
2)Define a cache manager. For ex, SynchronizedMaps, EHCache , Gemfire etc.

Here is the code sample:

	@FeignClient(url="localhost:8080/warehouse")
	public interface InventoryClient{
	
		@Cacheable(value="inventory", key="#sku")
		@RequestMapping("/inventory/{sku}")
		public @ResponseBody Item getInventoryItem(@PathVaribale Long sku);
	}

Problem with @Cacheable
-----------------------
Cacheable works great, but the decision of caching should be taken by the warehouse service. i.e. warehouse service should use expires and etag headers.

ETags
-----
Modern http-based caching header, works better than expires.

How ETag works
--------------
1)When client request for the resource, server responds with an etag. This ETag is a hash value calculated from the response string. This etag is an unique value. Both client and server saves this value associated with the request url.<br>
2)When client makes a request later for the same url, it sends the etag in the request header named "if-none-match".<br> 
3)Server calculates new hash.<br>
	a)If the hash value matches it returns 304. 304 means "not modified".<br>
	b)If not return, 200 , new content and new etag.

Note: The purpose of etag is to save bandwidth.
	
Implementing ETag
-----------------
Implementing ETag with spring is very easy. We need to create a filter bean in the configuration file like below.

	@Bean
	public Filter shallowEtagHeaderFilter(){
		return new ShallowEtagHeaderFilter();
	}

Etag client side
----------------
Client should save the etags, otherwise it is of no use. RestTemplate does not have the capability, but HttpClient does. Here is the client code:

	CacheConfig cacheConfig=CacheConfig.custom()
										.setMaxCacheEntries(1000)
										.setMaxObjectSize(8192)
										.build();
	
	CloseableHttpClient cachingClient = CachingHttpClients.custom()
										.setCacheConfig(cacheConfig)
										.build();
	
	RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(
										(cachingClient));

Note: The above RestTemplate can use Eureka clientId and can also load balance the services.

Note: Feign does not support caching.

Resource Expansion
------------------
Resource expansion can be seen in restful web services that follow HATEOAS(hypermedia as the engine of application state). For ex, If we have two rest resouces say team and players. The team sends a response which has link to players rest end point which the clients can follow. But for mobile devices multiple calls is an expensive task. If we have a mobile app that needs information of a team and players all at once, we should send this at one call for efficiency.

***
Note: API gateway has to follow this and expand the link instead of clients. That means effectively Gateway is un-doing the hateoas.

Traverson
---------
One option for resource expansion is a library called Traverson. This is actually a javascript library originally made for node js, used to traverse the links. There is a small part of it present in spring-hateoas artifact. We need the following dependencies in pom.xml for working with traverson.

	<dependency>
		<groupid>org.springframework.hateoas</groupid>
		<artifactid>spring-hateoas</artifactid>
	</dependency>
	<dependency>
		<groupid>com.jayway.jsonpath</groupid>
		<artifactid>json-path</artifactid>
	</dependency>

Usage:
------
1)Create Resources for all domain objects. For ex,
	
	import org.springframework.hateoas.Resource;
	import org.springframework.hateoas.Resources;

	public class PlayerResouces extends Resources<Resource<Player>>{
	}

Note: Resource is a class present in spring hateoas, which is a wrapper over domain objects. It provisions links to other resouces, so that your domain objects don't have to model that. It also understands HAL structure which is the convension that spring-data-rest has chosen.

Note: Resources knows to handle collection/array usage.	

2)Traverse code.
	
	Traverson traverson = new Traverson(new URI("http://localhost:8080/"),MediaTypes.HAL_JSON);
	PlayerResources playerResources = traverson.follow("$_links.team.href"
												,"$_embedded.team[0]._links.players.href")
												.toObject(PlayerResources.class);
	for(Resource<player> playerResource : playerResources.getContent()){
		Player player = playerResource.getContent();
		System.out.println(player);
	}

Note: Traverson doesn't have any integration with ribbon built-in. If we want client side load balancing, we need to call ribbon manually to obtain the url. Traverson can be passed with a RestTemplate, that can handle load balancing and caching etc.

Note: traverson.follow() first argument is the first json path expression used to locate the link that we want to follow. second argument is the subsequent expression for the link and follow that.

Note: In the above example, first expression gives us the list of teams. Second argument gives the players link of the first team(team[0]).

Limitations
-----------
1)Traverson is not doing expansion automatically as seen above. We have to manually explore the links we want and manually attach them.<br>
2)It works with HAL format and if you want anyother formats you need to write your own unmarshaller.<br>
3)No support of XML.

Other options
-------------
Spring data rest supports projection. Projection causes links to be inlined or pre-expanded. We can set this to be premanent or optional. We can trigger this using incoming http parameters.

Defining projections
--------------------
1)Define projection as an interface.<br>

	@Projection(name="inlinePlayers", types={Team.class})
	public interface InlinePlayers{
		String getName();
		String getLocation();
		String getMascotte();
		Set<Player> getPlayers();
	}

Note: Define a projection with an interface with properties(or methods) that you want to expose over rest. In the above example name,location, mascotte,players. These are actually the properties of the team.

Note: types attribute defines the parent object that this projection object applies to. In this case Team.

To access the players along with team, use the url pattern below.
	http://localhost:8080/teams/1?projection=inlinePlayers

Note: without projection parameter, it will return the hateoas behavior with the links.
	
Note: You can also annotate this interface with @Repository, if you want to return this all ways.

Limitations with projections
----------------------------
1)Works only with spring-data-rest.<br>
2)Works if projections are part of the same microservice.
	
Protocol Translation
--------------------
API gateway might have exposed rest over http for our mobile and desktop clients. But, our backend services might be asynchronous messaging services like jms,amqp or soap based services etc.


One solution is to use Adapters before those backend services. Spring provides a lot of adapters like JMSTemplate, AMQPTemplate , WebServiceTemplate(for SOAP) etc.

Better solution is to use spring integration in the API gateway itself. It is a powerful framework for enterprise integration patterns and in-memory messaging.


	
