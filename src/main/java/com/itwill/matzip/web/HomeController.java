package com.itwill.matzip.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwill.matzip.dto.TagRestaurantRequestDto;
import com.itwill.matzip.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
	
	private final RestaurantService restSvc;

	@GetMapping("/")
	public String home(Model model) {
		log.info("home()");
		
		List<TagRestaurantRequestDto> list = restSvc.getRestaurantByHashTag();
		
		model.addAttribute("tags", list);
		
		return "home";
	}
	
	@GetMapping("/layout/navigation")
	public String testNav() {
		return "test/navigationTest";
	}
}
