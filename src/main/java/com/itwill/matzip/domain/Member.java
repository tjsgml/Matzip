package com.itwill.matzip.domain;

import java.time.LocalDate;
import java.util.*;

import org.hibernate.annotations.*;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
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
	
	@Basic(optional = false)
	private String email;
	
	private String kakao_client_id;
	
	@Basic(optional = false)
	private LocalDate birth;
	
	
	@Basic(optional = false)
	private String nickname;
	
	//@ColumnDefault("'default.png'")
	private String img;
	
	@Basic(optional = false)
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
	
}
