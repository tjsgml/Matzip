package com.itwill.matzip.dto;

import java.util.List;
import java.util.Set;

import com.itwill.matzip.domain.Restaurant;

import lombok.*;

@Builder
@Data
public class TagRestaurantRequestDto {
	private Long tagId;
	private String tagKeyword;
	
	private List<MyPickRequestDto> rest;
	private int restLength;
}
