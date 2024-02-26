package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemberMainHeaderRequestDto {
	private String nickname;
	private String img;
	private Long pickCnt;
	private Long reviewCnt;
}
