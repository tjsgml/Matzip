package com.itwill.matzip.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.MemberRole;

public class MemberSecurityDto extends User implements OAuth2User {
	
	private String nickname;
	
	private static final long serialVersionUID = 1L;

	public MemberSecurityDto(String username, String password,String nickname, Collection<? extends GrantedAuthority> authorities) {
		super(username, password,authorities);
		
		this.nickname = nickname;
		
	}
	
	public static MemberSecurityDto fromEntity(Member entity) {
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(MemberRole role : entity.getRoles()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role.getAuthority());
			authorities.add(auth);
		}
		
		return new MemberSecurityDto(entity.getUsername(), entity.getPassword(),entity.getNickname(),authorities);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
}
