package com.itwill.matzip.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwill.matzip.dto.MemberSignupRequestDto;
import com.itwill.matzip.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberSvc;

	@GetMapping("/login")
	public void login() {
		log.info("Get - login()");
	}
	
	@GetMapping("/signup")
	public void signup() {
		log.info("Get - signup" );
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute MemberSignupRequestDto dto) {
		log.info("Post - signup(dto = {})", dto.toString());
		
		memberSvc.createMember(dto);
		
		return "redirect:/member/login";
	}
	
	@GetMapping("/checkid")
	@ResponseBody
	public ResponseEntity<String> checkId(@RequestParam(name = "username")String username){
		log.info("checkId(username = {})", username);
		
		String result = memberSvc.checkUsername(username);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/checknick")
	@ResponseBody
	public ResponseEntity<String> checkNick(@RequestParam(name="nickname") String nickname){
		log.info("checkNick(nickname = {})", nickname);
		
		String result = memberSvc.checkNickname(nickname);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/checkemail")
		public ResponseEntity<String> checkEmail(@RequestParam(name="email") String email){
		log.info("checkEmail(email = {})", email);
		
		String result = memberSvc.checkEmail(email);
		
		return ResponseEntity.ok(result);
	}
	
}
