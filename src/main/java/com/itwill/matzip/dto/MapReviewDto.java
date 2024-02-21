package com.itwill.matzip.dto;

import java.util.ArrayList;
import java.util.List;

import com.itwill.matzip.domain.ReviewImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapReviewDto {
	private Long id;
	
	private String content;
	
	private Integer flavorScore;
	
	private Integer serviceScore;
	
	private Integer priceScore;
	
	private List<String> reviewImages;
	
}
