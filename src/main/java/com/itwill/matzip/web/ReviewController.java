package com.itwill.matzip.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.dto.HashtagDto;
import com.itwill.matzip.dto.ReviewCreateDto;
import com.itwill.matzip.dto.ReviewUpdateDto;
import com.itwill.matzip.service.RestaurantService;
import com.itwill.matzip.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

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

        return "redirect:/rest/details?id=" + reviewDto.getRestaurantId();
    }


    // 리뷰 수정
    @GetMapping("/update/{reviewId}") 
    public String reviewUpdate(@PathVariable("reviewId") Long reviewId, Model model) {
        // 리뷰 정보 조회 
        Review review = reviewSvc.findReviewById(reviewId);
        List<String> reviewImages = reviewSvc.getReviewImg(reviewId); // 리뷰 이미지
        if (review == null) {
            // 리뷰 정보가 없으면
            return "redirect:/error";
        }
        
        List<HashtagDto> hashtags = review.getHashtags().stream()
        		.map(h-> new HashtagDto(h.getId(), h.getKeyword(), h.getHtCategory().getName()))
        		.collect(Collectors.toList());

        // 리뷰와 연관된 레스토랑 정보 가져옴
        Restaurant restaurant = restaurantSvc.findOneRest(review.getRestaurant().getId());
        
        // 리뷰 정보, 레스토랑 정보 추가
        model.addAttribute("restaurantName", restaurant.getName());
        model.addAttribute("restaurantId", restaurant.getId());
        log.info("restaurantId={}", restaurant.getId());
        model.addAttribute("review", review);
        model.addAttribute("reviewImages", reviewImages);
        model.addAttribute("hashtags", hashtags);
        return "review/update"; // 리뷰 수정 페이지
    }
    
    @PostMapping("/update/{reviewId}")
    public String updateReview(@PathVariable("reviewId") Long reviewId, @ModelAttribute ReviewUpdateDto reviewDto, RedirectAttributes redirectAttributes) {
        try {
            if (reviewDto.getDeleteImageUrls() == null) {
                reviewDto.setDeleteImageUrls(new ArrayList<>());
            }

            if (reviewDto.getDeleteHashtagIds() == null) {
            	log.info("reviewDto.getDeleteHashtagIds() 널이다 이자식아=", reviewDto.getDeleteHashtagIds());
                reviewDto.setDeleteHashtagIds(new ArrayList<>());
            }

            reviewSvc.updateReview(reviewId, reviewDto);
            log.info("reviewId={}", reviewId);
            log.info("restaurantId={}", reviewDto.getRestaurantId());

            return "redirect:/rest/details?id=" + reviewDto.getRestaurantId();
//            return "redirect:/review/update/" + reviewId;
        } catch (Exception e) {
            log.info("reviewId={}", reviewId);
            e.printStackTrace();

            return "redirect:/review/update/" + reviewId;
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
    	log.info("deleteReview()");
        try {
            reviewSvc.deleteReview(reviewId);
            return ResponseEntity.ok().build(); // 성공 응답
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류 발생!");
        }
    }




    
    
    
    
    //레스트 컨트롤러-------------------------------
    //내가 쓴 리뷰의 이미지 가져오기
    @ResponseBody
    @GetMapping("/img/{reviewId}")
    public ResponseEntity<List<String>> getReviewImg(@PathVariable("reviewId") Long reviewId){
    	List<String> list = reviewSvc.getReviewImg(reviewId);

    	return ResponseEntity.ok(list);
    }
    
    
    
}
