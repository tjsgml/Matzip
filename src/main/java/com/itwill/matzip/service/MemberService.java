package com.itwill.matzip.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.MemberRepository;
import com.itwill.matzip.dto.MemberSecurityDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService{

	private final MemberRepository memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.info("loadUserByUsername(username={})", username);
		
		Optional<Member> opt = memberDao.findByUsername(username);
		if(opt.isPresent()) {
			return MemberSecurityDto.fromEntity(opt.get());
		}else {
			throw new UsernameNotFoundException(username + " 찾을 수 없음");
		}
	}
	
	
}
