package com.itwill.matzip.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewWithHashtagsDto {
	private Long id;
    private String content;
    private List<HashtagCategoryDto> categorizedHashtags;

}
