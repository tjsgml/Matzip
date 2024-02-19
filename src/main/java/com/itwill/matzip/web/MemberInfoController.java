package com.itwill.matzip.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.matzip.dto.MemberProfileInfoRequestDto;
import com.itwill.matzip.service.MemberInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/memberinfo")
@Controller
public class MemberInfoController {
	
	private final MemberInfoService miSvc;

	@GetMapping("/mymain")
	public void myMain(Principal principal) {
		log.info("마이 페이지 [마이픽]");
		
		MemberProfileInfoRequestDto dto = miSvc.getProfileInfo(principal.getName().toString());
	}

	@GetMapping("/myreview")
	public void myReview() {
		log.info("마이 페이지 [리뷰]");
	}
	
	@GetMapping("/profilemodify")
	public void profileModify() {
		log.info("마이 페이지 [프로필 수정]");
	}
	
	@GetMapping("/pwdmodify")
	public void pwdmodify() {
		log.info("마이페이지 [비밀번호 수정]");
	}
}
