package com.itwill.matzip.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ReviewCreateDto {
	
	private int tasteRating;
    private int priceRating;
    private int serviceRating;
    private String reviewContent;
    private List<String> visitPurposeTags;
    private List<String> moodTags;
    private List<String> convenienceTags;
    private MultipartFile[] images;
    private Long restaurantId;
    private Long memberId;
    

    
    
	
}
