package com.itwill.matzip.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/")
	public String home() {
		log.info("home()");
		
		return "home";
	}
}
