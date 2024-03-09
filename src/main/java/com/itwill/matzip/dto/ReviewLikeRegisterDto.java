package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewLikeRegisterDto {
	private Long memberId;
    private Long reviewId;

}
