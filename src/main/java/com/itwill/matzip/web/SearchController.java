package com.itwill.matzip.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/search")
public class SearchController {
	
	@GetMapping("/all")
	public String showSearchList(@RequestParam("keyword") String keyword, Model model) {
		
		log.info("@@@@@키워드={}",keyword);
		
		model.addAttribute("keyword",keyword);
		
		return "/restaurant/searchList";
		
	}
}
