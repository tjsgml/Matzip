package com.itwill.matzip.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest")
public class RestaurantController {
	private final RestaurantService restSvc;
	
	
	//details.html 보여주기(기본적인 음식점 정보 넣기)
	@GetMapping("/details")
	public String showdetails(@RequestParam(name="id")Long restId,Model model) {
		
		Restaurant rest = restSvc.findOneRest(restId);
		
		model.addAttribute("rest",rest);
		
		log.info(rest.toString());
		
		return "restaurant/details";
	}
	
	//js로 음식점 정보 전달
	@GetMapping("/details/{restId}")
	public ResponseEntity<Restaurant> deliverMap(@PathVariable("restId") Long restId){
		
		Restaurant rest = restSvc.findOneRest(restId);
		
		return ResponseEntity.ok(rest);
	}
	//영업시간 정보 가져오기
	@GetMapping("/details/businessHour/{restId}")
	public ResponseEntity<List<BusinessHour>> findBusinessHour(@PathVariable("restId") Long id){
		log.info("findBusinessHour 호출");
		List<BusinessHour> bsHourList = restSvc.findBsHour(id);
		
		return ResponseEntity.ok(bsHourList);
	}
	//메뉴 정보 가져오기
	@GetMapping("/details/menu/{restId}")
	public ResponseEntity<List<Menu>> findMune(@PathVariable("restId") Long id){
		log.info("@@@@@ findMenu() 호출");
		
		List<Menu> menuList = restSvc.findMenus(id);
		
		log.info("menuList={}",menuList);
		
		return ResponseEntity.ok(menuList);
	}
}
