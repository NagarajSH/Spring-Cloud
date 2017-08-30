package com.learning.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;


public abstract class WordDaoImpl implements WordDao {

	@Autowired 
	LoadBalancerClient loadBalancer;
	
	public abstract String getPartOfSpeech();
	
	public String getWord() {
		ServiceInstance instance = loadBalancer.choose(getPartOfSpeech());
   		return new RestTemplate().getForObject(instance.getUri(),String.class);
	}
	
}