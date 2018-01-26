Spring cloud Ribbon setup
-------------------------
1)Go to sentence service and add the following dependency to pom.xml<br>
Add the org.springframework.cloud / spring-cloud-starter-ribbon dependency.<br>
2)Go to Application.java. Create a new @Bean method that instantiates and returns a new RestTemplate. The @Bean method should also be annotated with @LoadBalanced - this will associate the RestTemplate with Ribbon. Code should look something like this:

	  //  This "LoadBalanced" RestTemplate 
	  //  is automatically hooked into Ribbon:
	  @Bean 
	  @LoadBalanced
	  RestTemplate restTemplate() {
	      return new RestTemplate();
	  }
3)Open SentenceController.java. Replace the @Autowired DiscoveryClient with an @Autowired RestTemplate. <br>
4)Refactor the code in the getWord method. Use your restTemplate's getForObject method to call the given service. The first argument should be a concatenation of "http://" and the given service ID. The second argument should simply be a String.class; we want the restTemplate to yield a String containing whatever was returned to the server. The call should look like this:

  	return template.getForObject("http://" + service, String.class);
5)Run the project. Test it to make sure it works by opening http://localhost:8020/  

Multiple clients
----------------
To see the real advantage of ribbon load balancing we need to have multiple instances of some service say noun.<br>
1)Modify the words in application.yml file of service-noun project with the following<br>
	words:  icicle,refrigerator,blizzard,snowball
2)Modify the port also to not conflict the existing one.

Wait until this new service is also detected by Eureka, an then open the url http://localhost:8020/ . Now keep refreshing to see the words reflecting from different intances.



  
