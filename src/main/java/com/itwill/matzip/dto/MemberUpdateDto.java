package com.itwill.matzip.dto;

import java.time.LocalDate;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.Gender;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberUpdateDto {
	private String username;
	private String email;
	private String nickname;
	private LocalDate birth;
	private Gender gender;
	
	public Member toEntity() {
		return Member.builder()
										.username(username)
										.email(email)
										.nickname(nickname)
										.birth(birth)
										.gender(gender)
										.build();
	}
}
