package com.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/")
@RefreshScope
public class RatesController {

	@Value("${rate}")
	private String rate;
	
	@Value("${lanecount}")
	private String laneCount;
	
	@Value("${tollstart}")
	private String tollStart;
	
	@Value("${connstring}")
	private String connString;
	@RequestMapping("/rate")
	public String getRate(Model m){
		m.addAttribute("rateamount",rate);
		m.addAttribute("lanes",laneCount);
		m.addAttribute("tollstart",tollStart);
		m.addAttribute("connstring",connString);
		return "rateview"; 
	}
}
