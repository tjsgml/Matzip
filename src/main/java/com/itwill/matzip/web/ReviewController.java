package com.itwill.matzip.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.matzip.dto.ReviewCreateDto;
import com.itwill.matzip.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/create")
    public String reviewCreateForm() {
    	log.info("GET - reviewCreateForm");
        return "review/create";
    }

    @PostMapping("/register")
    public String handleReviewSubmit(@ModelAttribute ReviewCreateDto reviewDto, RedirectAttributes redirectAttributes) {
        try {
            reviewService.saveReview(reviewDto);
            log.info("reviewDto", reviewDto.toString());
            redirectAttributes.addFlashAttribute("message", "리뷰 등록 성공!");
        } catch (Exception e) {
        	log.info("review register 실패", reviewDto);
            redirectAttributes.addFlashAttribute("errorMessage", "리뷰 등록 실패: " + e.getMessage());
            return "redirect:/review/create";
        }
        return "redirect:/reviews";
    }
}
