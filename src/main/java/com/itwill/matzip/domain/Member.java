package com.itwill.matzip.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;

import com.itwill.matzip.domain.enums.Gender;
import com.itwill.matzip.domain.enums.MemberRole;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @NaturalId
    @Basic(optional = false)
    @Column(updatable = false)
    private String username;

    @Basic(optional = false)
    private String password;

    private String email;

    private String kakaoClientId;

    private LocalDate birth;

    private String nickname;

	private String img;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @ToString.Exclude
	@JsonInclude
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    //member 메서드 ----------------------------------------------------
	//권한 추가
	public Member addRole(MemberRole role) {
		roles.add(role);
		return this;
	}

	//권한 다 지움
	public Member clearRoles() {
		roles.clear();
		return this;
	}

	//회원 정보 수정
	public Member memUpdate(Member dto) {
		this.email = dto.getEmail();
		this.nickname = dto.getNickname();
		this.birth = dto.getBirth();
		this.gender = dto.getGender();
		return this;
	}

	//비밀번호 변경
	public Member pwdUpdate(String pwd) {
		this.password = pwd;
		return this;
	}
	
	//프로필 이미지 변경
	public Member profileImgUpdate(String profileUrl) {
		this.img = profileUrl;
		return this;
	}
}