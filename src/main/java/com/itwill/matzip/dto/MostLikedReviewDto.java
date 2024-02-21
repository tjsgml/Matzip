package com.itwill.matzip.dto;

import com.itwill.matzip.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MostLikedReviewDto {
	private String content;
	private String nickname;
	private Long totalLikes;
}
