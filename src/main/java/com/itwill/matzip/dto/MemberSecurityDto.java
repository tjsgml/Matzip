package com.itwill.matzip.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.MemberRole;

public class MemberSecurityDto extends User {

	public MemberSecurityDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	public static MemberSecurityDto fromEntity(Member entity) {
		List<SimpleGrantedAuthority> authorities =
				entity.getRoles().stream()
				.map((x)->new SimpleGrantedAuthority(x.toString())).toList();
		
		for(MemberRole role : entity.getRoles()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role.getAuthority());
			authorities.add(auth);
		}
		
		return new MemberSecurityDto(entity.getUsername(), entity.getPassword(), authorities);
	}

}
