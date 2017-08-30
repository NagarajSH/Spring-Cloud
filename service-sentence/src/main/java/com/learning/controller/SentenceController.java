package com.learning.controller;

import java.net.URI;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.learning.dao.NounClient;
import com.learning.service.SentenceService;

@RestController
public class SentenceController {

	//@Autowired
	//private DiscoveryClient client;
	
	
	
	/**
	 * This Loadbalanced RestTemplate is automatically hooked into ribbon
	 * @return
	 */
	//@Autowired
	//RestTemplate template;
	
	//@Autowired
	//NounClient client;

	/*@RequestMapping("/")
	public String getSentence() {
		return 
			      getWord("SERVICE-SUBJECT") + " "
			      + getWord("SERVICE-VERB") + " "
			      + getWord("SERVICE-ARTICLE") + " "
			      + getWord("SERVICE-ADJECTIVE") + " "
			      + getWord("SERVICE-NOUN") + "."
			     ; 
			      
	}*/

	/**
	 * Using DiscoveryClient to get ServiceInstance
	 * @param service
	 * @return
	 */
	/*public String getWord(String service) {
		List<ServiceInstance> list = client.getInstances(service);
		if (list != null && list.size() > 0) {
			URI uri = list.get(0).getUri();
			if (uri != null) {
				return (new RestTemplate()).getForObject(uri, String.class);
			}
		}
		return null;
	}*/
	
	/**
	 * Using ribbon to get ServiceInstance
	 * @param service
	 * @return
	 */
	/*public String getWord(String service){
		return template.getForObject("http://"+service, String.class);
	}*/
	
	@Autowired 
	SentenceService sentenceService;
	

	
	/**
	 * Display a small list of Sentences to the caller:
	 */
	@RequestMapping("/sentence")
	public String getSentences() {
	  return 
		"<h3>Some Sentences</h3><br/>" +	  
		sentenceService.buildSentence() + "<br/><br/>" +
		sentenceService.buildSentence() + "<br/><br/>" +
		sentenceService.buildSentence() + "<br/><br/>" +
		sentenceService.buildSentence() + "<br/><br/>" +
		sentenceService.buildSentence() + "<br/><br/>"
		;
	}

}
