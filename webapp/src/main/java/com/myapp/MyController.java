package com.myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
	
	@RequestMapping("/greeting")
	public String greeting(@RequestParam String name, Model m){
		m.addAttribute("name",name);
		return "index";
	}

}
