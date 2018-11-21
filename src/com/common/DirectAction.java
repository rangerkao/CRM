package com.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DirectAction {
	
	@RequestMapping("/directive/{directive}")
	public ModelAndView directivePage(@PathVariable String directive) {
		
 		return new ModelAndView("/directive/"+directive.replace(".jsp", ""));
	}
	@RequestMapping("/pages/{page}")
	public ModelAndView subPage(@PathVariable String page) {
		
 		return new ModelAndView("/pages/"+page.replace(".jsp", ""));
	}

}
