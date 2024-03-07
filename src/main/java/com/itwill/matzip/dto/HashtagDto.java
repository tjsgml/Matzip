package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HashtagDto {
	private Long hashtagId;
	private String keyword;
	private String hashtagCategory;
	
}
