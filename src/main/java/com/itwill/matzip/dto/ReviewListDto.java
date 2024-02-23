package com.itwill.matzip.dto;

import java.util.List;
import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewListDto {
	private Long id;
	private String content;
	private Integer flavorScore;
	private Integer serviceScore;
	private Integer priceScore;
	private String formattedRegisterDate; // reviewRegisterDate DateTimeUtil사용할거라 String 타입으로..
	private String memberNickname;
	private String memberImg;
	private List<String> reviewImages;
	private Set<String> hashtags;

}
