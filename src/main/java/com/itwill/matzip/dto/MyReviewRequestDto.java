package com.itwill.matzip.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class MyReviewRequestDto {
	
	private String mainImg;	//음식점 메인에 들어갈 사진
	private Long restaurantId;
	private String restaurantName;
	private String categoryName;
	private String location;
	
	private Double totalAllReviewRating;	//모든 리뷰의 총점을 구함.. 여러 개의 리스트여도 똑같은 값을 가짐
	
	private Long reviewId;
	private LocalDateTime createTime;
	private Double flavorScore; 
	private Double serviceScore;
	private Double priceScore;
	private String content;
	private List<String> reviewImg;	//내가 등록한 리뷰의 사진들
}
