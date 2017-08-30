package com.learning.dao;

//import com.learning.domain.Word;

public interface WordDao {
	
	static final String SUBJECT = "SERVICE-SUBJECT";
	static final String VERB = "SERVICE-VERB";
	static final String ARTICLE = "SERVICE-ARTICLE";
	static final String ADJECTIVE = "SERVICE-ADJECTIVE";
	static final String NOUN = "SERVICE-NOUN";
	
	String getWord();

}
