package com.itwill.matzip.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Restaurant;
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
	
	
}
