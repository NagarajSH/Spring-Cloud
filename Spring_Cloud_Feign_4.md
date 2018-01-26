What is spring cloud feign?
Ans: Feign is a declarative rest client from netflix, which allows to write calls to rest services with no implementation code.

Note: Feign is an alternative to RestTemplate but it is even easier. Spring cloud provides easy wrapper for using Feign.

We know spring rest template provides easy way to call Rest services. For ex,

		RestTemplate template = new RestTemplate();
		String url = "http://localhost:8011/skuService";
		Sku sku = template.getForObject(url,Sku.class, 473217);
	
Note: Still the above code needs to be written and unit tested.

Feign
-----	
To work with Fiegn we need provide an interface for the rest client code. This interface needs to be annotated with Feign annotations and Spring MVC annotations.

Note: Instead of spring MVC other alternatives like jax/rs also pluggable.

How it works
------------
Spring cloud scan the interface and annotations and provides the implementation code at run-time to call rest services. Here is the sample interface.

	@FeignClient(url="http://localhost:8080/warehouse"
	public interface InventoryClient{
		
		@RequestMapping(method=RequestMethod.GET,value="/inventory")
		List<Item> getItems();
		
		@RequestMapping(method=RequestMethod.POST,value="/inventory/{sku}", consumes="application/json")
		void update(@PathVariable("sku") Long sku, @RequestBody Item item);
	}

*****
Note: Annotate the main class with @EnableFeignClients. Now you can autowire inventoryClient wherever one is needed.

Note: We need Feign dependency at run-time not at compile time.

	<dependencies>
		<dependency>
			<groupid>org.springframework.cloud</groupid>
			<artifactid>spring-cloud-starter-feign</artifactid>
		</dependency>
	</dependencies>

	
