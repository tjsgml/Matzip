package com.itwill.matzip.domain;

import java.time.LocalDate;
import java.util.Set;

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
public class Member {

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

	// @ColumnDefault("'default.png'")
	private String img;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Builder.Default
	@ToString.Exclude
	@ElementCollection(fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private Set<MemberRole> roles = new HashSet<>();

	public Member addRole(MemberRole role) {
		roles.add(role);
		return this;
	}

	public Member clearRoles() {
		roles.clear();
		return this;
	}

	public Member socialMemUpdate(Member dto) {
		this.email = dto.getEmail();
		this.nickname = dto.getNickname();		/*02.14 수정 - getEmail()로 되어 있었음*/
		this.birth = dto.getBirth();
		this.gender = dto.getGender();

		return this;
	}

	public Member pwdUpdate(String pwd) {
		this.password = pwd;

		return this;
	}
}
