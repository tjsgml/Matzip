package com.itwill.matzip.dto;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwill.matzip.domain.Gender;
import com.itwill.matzip.domain.Member;

import lombok.Data;

@Data
public class MemberSignupRequestDto {
	private String username;
	private String password;
	private String email;
	private String nickname;
	private LocalDate birth;
	private Gender gender;
	
	public Member toEntity(PasswordEncoder encoder) {
		return Member.builder()
										.username(username)
										.password(encoder.encode(password))
										.email(email)
										.nickname(nickname)
										.birth(birth)
										.gender(gender)
										.build();
	}
}
