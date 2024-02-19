package com.itwill.matzip.service;

import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.MemberProfileInfoRequestDto;
import com.itwill.matzip.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberInfoService {

	MemberRepository memDao;
	
	public MemberProfileInfoRequestDto getProfileInfo(String username) {
		log.info("MemberInfoService : username - {}",username);

		Member member = memDao.findByUsername(username).orElseThrow();
		
		return null;
	}

}
