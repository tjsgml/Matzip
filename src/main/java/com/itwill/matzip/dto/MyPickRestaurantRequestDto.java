package com.itwill.matzip.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MyPickRestaurantRequestDto {
	private Long memberId;
	private Long pickId;
	private String imgUrl;
	private Long restaurantId;
	private String restaurantName;
	private String categoryName;
	private String location;
	private Double totalSart;
	private Long reviewAllCount;
}
