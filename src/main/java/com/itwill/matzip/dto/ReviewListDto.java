package com.itwill.matzip.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class ReviewListDto {
	private Long id;
	private String content;
	private Integer flavorScore;
	private Integer serviceScore;
	private Integer priceScore;
	private LocalDateTime reviewRegisterDate;
	private String memberNickname;
	private String memberImg;
	private List<String> reviewImages;
	private Set<String> hashtags;

}
