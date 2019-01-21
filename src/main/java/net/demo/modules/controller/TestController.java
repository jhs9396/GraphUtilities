package net.demo.modules.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * index page test controller
 * 
 * @author		HyeonSu Jeon
 * @version		0.2
 * @since		0.1
 */
@Controller
public class TestController {
	
	/**
	 * index page call method
	 * 
	 * @return	index.html call 
	 */
	@RequestMapping(value="/index")
	public String index() {
		return "index";		
	}
	
	/**
	 * main page call method
	 * 
	 * @return	main.html call
	 */
	@RequestMapping(value="/main")
	public String main() {
		return "main";		
	}

	/**
	 * d3 page call method
	 *
	 * @return d3.html call
	 */
	@RequestMapping(value="/d3")
	public String d3() { return "d3"; }
}
