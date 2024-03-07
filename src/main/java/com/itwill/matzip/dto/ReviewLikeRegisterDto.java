package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewLikeRegisterDto {
	private Long memberId;
    private Long reviewId;

}
