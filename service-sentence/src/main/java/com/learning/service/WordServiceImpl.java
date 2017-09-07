package com.learning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.dao.AdjectiveClient;
import com.learning.dao.ArticleClient;
import com.learning.dao.NounClient;
import com.learning.dao.SubjectClient;
import com.learning.dao.VerbClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class WordServiceImpl implements WordService{

	@Autowired VerbClient verbClient;
	@Autowired SubjectClient subjectClient;
	@Autowired ArticleClient articleClient;
	@Autowired AdjectiveClient adjectiveClient;
	@Autowired NounClient nounClient;
	
	
	@Override
	public String getSubject() {
		return subjectClient.getWord();
	}
	
	@Override
	public String getVerb() {
		return verbClient.getWord();
	}
	
	@Override
	public String getArticle() {
		return articleClient.getWord();
	}
	
	@Override
	@HystrixCommand(fallbackMethod="getDefaultAdjective")
	public String getAdjective() {
		return adjectiveClient.getWord();
	}
	
	public String getDefaultAdjective(){
		return "";
	}
	
	@Override
	public String getNoun() {
		return nounClient.getWord();
	}
	
	

}
