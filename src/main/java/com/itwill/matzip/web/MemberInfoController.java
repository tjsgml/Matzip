package com.itwill.matzip.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/memberinfo")
@Controller
public class MemberInfoController {

	@GetMapping("/mymain")
	public void myMain() {
		log.info("마이 페이지 [마이픽]");
	}

	@GetMapping("/myreview")
	public void myReview() {
		log.info("마이 페이지 [리뷰]");
	}
	
	@GetMapping("/profilemodify")
	public void profileModify() {
		log.info("마이 페이지 [프로필 수정]");
	}
}