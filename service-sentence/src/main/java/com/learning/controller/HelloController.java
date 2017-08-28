package com.learning.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping("/")
	public String getSentence() {
		return 
			      getWord("SERVICE-SUBJECT") + " "
			      + getWord("SERVICE-VERB") + " "
			      + getWord("SERVICE-ARTICLE") + " "
			      + getWord("SERVICE-ADJECTIVE") + " "
			      + getWord("SERVICE-NOUN") + "."
			      ;
	}

	public String getWord(String service) {
		List<ServiceInstance> list = client.getInstances(service);
		if (list != null && list.size() > 0) {
			URI uri = list.get(0).getUri();
			if (uri != null) {
				return (new RestTemplate()).getForObject(uri, String.class);
			}
		}
		return null;
	}

}
