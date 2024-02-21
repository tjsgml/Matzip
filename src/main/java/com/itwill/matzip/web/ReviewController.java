package com.itwill.matzip.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.ReviewCreateDto;
import com.itwill.matzip.service.MemberInfoService;
import com.itwill.matzip.service.RestaurantService;
import com.itwill.matzip.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {
	private final MemberInfoService miSvc;		//memberinfo 서비스 빈 생성

    private final ReviewService reviewSvc;
    
    @Autowired
    private RestaurantService restaurantSvc;

    // 리뷰 등록 폼
    @GetMapping("/create")
    public String reviewCreateForm(@RequestParam("restaurantId") Long restaurantId, Model model) {
        log.info("GET - reviewCreateForm - restaurantId: {}", restaurantId);
        
        Restaurant restaurant = restaurantSvc.findOneRest(restaurantId);
        model.addAttribute("restaurantName", restaurant.getName());
        model.addAttribute("restaurantId", restaurantId);
        return "review/create";
    }

    // 리뷰 등록
    @PostMapping("/register")
    public String reviewRegister(@ModelAttribute ReviewCreateDto reviewDto, RedirectAttributes redirectAttributes) {
        try {
            reviewSvc.saveReview(reviewDto);
            redirectAttributes.addFlashAttribute("message", "리뷰 등록 성공!");
        } catch (Exception e) {
        	log.info("review register 실패", reviewDto);
        	e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "리뷰 등록 실패: " + e.getMessage());
            return "redirect:/review/create?restaurantId=" + reviewDto.getRestaurantId(); // 리다이렉트시 레스토랑ID 쿼리파라미터로 추가

        }
        
        return "redirect:/map/list";
    }
    
    
    @ResponseBody
    @GetMapping("/img/{reviewId}")
    public ResponseEntity<List<String>> getReviewImg(@PathVariable("reviewId") Long reviewId){
    	List<String> list = miSvc.getReviewImg(reviewId);
    	
    	return ResponseEntity.ok(list);
    }
}
