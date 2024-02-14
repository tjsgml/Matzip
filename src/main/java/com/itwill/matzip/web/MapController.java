package com.itwill.matzip.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/map")
public class MapController {
	private final RestaurantService restSvc;
	
	//map.html 보여주기
	@GetMapping("/list")
	public String map(Model model) {
		log.info("@@@@ home()");
		
		return "restaurant/map";
	}
	
	//지도 전체 리스트 가져오기
	@GetMapping("/all")
	public ResponseEntity<List<Restaurant>> map(){
		log.info("@@@@ map() 호출");
		
		List<Restaurant> list = restSvc.findAllMaps();
		
		
		return ResponseEntity.ok(list);
	}
	
}
