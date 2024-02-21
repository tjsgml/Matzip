package com.itwill.matzip.dto;

import java.time.LocalDate;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.Gender;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberSocialUpdateDto {
	private String username;
	private String email;
	private String nickname;
	private LocalDate birth;
	private Gender gender;
	
	public Member toEntity() {
		return Member.builder()
										.username(username) 		/*02.14 수정 - kakaoClient로 되어 있었음*/
										.email(email)
										.nickname(nickname)
										.birth(birth)
										.gender(gender)
										.build();
	}
}
