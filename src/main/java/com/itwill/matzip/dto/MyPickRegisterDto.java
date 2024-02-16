package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MyPickRegisterDto {
	private Long memberId;
	private Long restId;
}
