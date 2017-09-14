package com.learning.controller;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConfigurationProperties(prefix="adjective")
public class HelloController {
	
	//@Value("${words}")
	private String words;

	
	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}


	@RequestMapping("/")
	public String getWord(){
		String[] wordArray = words.split(",");
	    int i = (int)Math.round(Math.random() * (wordArray.length - 1));
	    return wordArray[i];
	}

}
