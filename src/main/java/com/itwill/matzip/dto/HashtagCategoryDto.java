package com.itwill.matzip.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HashtagCategoryDto {
	private String category;
    private Set<String> hashtags;

}
