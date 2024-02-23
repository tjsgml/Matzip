package com.itwill.matzip.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.dto.MapReviewDto;
import com.itwill.matzip.dto.MostLikedReviewDto;
import com.itwill.matzip.service.MapService;
import com.itwill.matzip.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/map")
public class MapController {
	private final RestaurantService restSvc;
	
	private final MapService mapSvc;
	
	//map.html 보여주기 + 카테고리 목록 가져오기.
	@GetMapping("/list")
	public String map(Model model) {
		log.info("@@@@ home()");
		List<Category> list = mapSvc.getAllCategory();
		model.addAttribute("categories",list);
		return "restaurant/map";
	}
	
	//지도 전체 리스트 가져오기
	@GetMapping("/all")
	public ResponseEntity<List<Restaurant>> map(){
		log.info("@@@@ map() 호출");
		
		List<Restaurant> list = restSvc.findAllMaps();
		
		
		return ResponseEntity.ok(list);
	}
	//음식점의 리뷰들 가져오기
	@GetMapping("/reviews/{id}")
	public ResponseEntity<List<MapReviewDto>> getReviews(@PathVariable(name = "id") Long id){
		List<MapReviewDto> list = mapSvc.getReviews(id);
		if(list == null) {
			return ResponseEntity.ok(null);
		}else {
			return ResponseEntity.ok(list);
		}
	}
	//리뷰 내용들가져오기.
	@GetMapping("/getMostLikedReview/{id}")
	public ResponseEntity<List<MostLikedReviewDto>> getMostLikedReview(@PathVariable(name = "id") Long id) {
		List<MostLikedReviewDto> list = mapSvc.findMostLikedReviewByRestaurantId(id);
		log.info(list.toString());
		return ResponseEntity.ok(list);
	}
	//음식점 좋아요 수 가져오기.
	@GetMapping("getTotalMypicks/{id}")
	public ResponseEntity<Integer> getTotalMyPicks(@PathVariable(name = "id")Long id){
		log.info("마이 픽 호출");
		Integer total = mapSvc.getTotalMyPicks(id);
		if(total != null) {
			return ResponseEntity.ok(total);
		}else {
			return ResponseEntity.ok(null);
		}
	}
	//카테고리에 따른 음식점 리스트 가져오기
	@GetMapping("/getRestaurantsByCategory")
	public ResponseEntity<List<Restaurant>> getRestaurantsByCategory(@RequestParam("category") Integer categoryId){
		log.info("@@@ getRestaurantsByCategory 호출 categoryId={}",categoryId);
		List<Restaurant> list = mapSvc.getRestaurantsByCategory(categoryId);
		log.info("@@@@@ getRestaurantsByCategory-> list={}",list);
		return ResponseEntity.ok(list);
	}
	//전체 리스트에서 검색하기.
	@GetMapping("/searchAllByKeyword")
	public ResponseEntity<List<Restaurant>> searchAllByKeyword(@RequestParam("keyword") String keyword){
		List<Restaurant> list = mapSvc.searchAllByKeyword(keyword);
		
		return ResponseEntity.ok(list);
	}
	
	//카테고리 별 검색하기.
	@GetMapping("/searchByCategoryAndKeyword")
	public ResponseEntity<List<Restaurant>> searchByCategoryAndKeyword(@RequestParam("keyword") String keyword,@RequestParam("category") Integer category){
		List<Restaurant> list = mapSvc.searchByCategoryAndKeyword(category,keyword);
		log.info("@@@@카테고리별 검색 결과={}",list);
		return ResponseEntity.ok(list);
	}
}
