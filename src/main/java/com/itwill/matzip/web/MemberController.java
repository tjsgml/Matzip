package com.itwill.matzip.web;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.MemberSecurityDto;
import com.itwill.matzip.dto.MemberSignupRequestDto;
import com.itwill.matzip.dto.MemberUpdateDto;
import com.itwill.matzip.service.MailService;
import com.itwill.matzip.service.MemberService;
import com.itwill.matzip.service.SocialMemberService;


import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberSvc;
	private final SocialMemberService socialSvc;
	private final MailService mailService;

	// 로그인 폼으로 이동
	@GetMapping("/login")
	public String login(Principal principal) {
		log.info("Get - login() 호출");
		if(principal == null) {
			return "member/login";
		}else {
			return "redirect:/";
		}
	}
	
	
	// 회원가입 폼으로 이동
	@GetMapping("/signup")
	public void signup() {
		log.info("Get - signup");
	}

	// 회원 가입
	@PostMapping("/signup")
	public String signup(@ModelAttribute MemberSignupRequestDto dto) {
		log.info("Post - signup(dto = {})", dto.toString());

		memberSvc.createMember(dto);

		return "redirect:/member/login";
	}

	// 소셜 로그인 회원 추가 정보 입력 폼으로 이동
	@PreAuthorize("hasRole('GUEST')")
	@GetMapping("/addinfo")
	public void addinfo() {
		log.info("추가 정보 창 띄움");
	}

	// 소셜 첫가입 회원 추가 정보 추가/기존 회원의 정보 수정
	@PostMapping({"/addinfo", "/modifyInfo"})
	public String updateMemberInfo(MemberUpdateDto dto, 
																		@AuthenticationPrincipal MemberSecurityDto msd,
																		ServletRequest request) {
		log.info("updateMemberInfo(dto : {})", msd.getUserid());
		
		HttpServletRequest req = (HttpServletRequest)request;
		String target = req.getRequestURI();
				
		socialSvc.updateMember(dto, msd);
		
		if (target.equals("/member/modifyInfo")) {
			return "redirect:/memberinfo/mymain";
		}
		
		return "redirect:/";
	}

	// 계정찾기 폼으로 이동
	@GetMapping("/forgot")
	public void findAccountForm() {
		log.info("Get - findAccountForm()");
	}

	// 계정찾기에서 이메일 인증 발송 -> 이메일 성공적으로 발송했다는 안내 표시
	@PostMapping("/forgot")
	public String findAccount(@RequestParam("email") String email, RedirectAttributes reAtt, HttpSession session) {
		log.info("Post - findAccount(email : {})", email);

		Member m = memberSvc.checkEmail(email);

		if (m != null) {			// 이메일에 해당하는 유저 존재
			if(m.getKakaoClientId()==null) {			//카카오 사용자가 아님
				reAtt.addFlashAttribute("email", m.getEmail());
				session.setAttribute("username", m.getUsername());

				mailService.sendHtmlEmail(m);

				return "redirect:/member/forgot";
			}else {		//카카오 사용자임
				return "redirect:/member/forgot?another";
			}
		} else {			// 이메일에 해당하는 유저 존재하지 않음
			return "redirect:/member/forgot?error";
		}
	}

	//비밀번호 변경 폼으로 이동
	@GetMapping("/password")
	public String passwordForm(@RequestParam(required = false, name = "key") String key, HttpSession session,
			Model model) {
		log.info("Get - changePassword(key = {}, session[username] = {})", key, session.getAttribute("username"));

		String valid = "N";

		if (key != null) {
			String username = session.getAttribute("username").toString();

			boolean result = memberSvc.authKey(key, username);

			if (result) {
				valid = "Y";
			}
		}
		model.addAttribute("valid", valid);

		return "member/password";
	}

	//비밀번호 찾기로 해서 비밀번호 변경하기
	@PostMapping("/successpwd")
	public String changePassword(@ModelAttribute("password") String pwd, HttpSession session) {
		log.info("Post - changePassword (password : {}, username : {})", pwd,
				session.getAttribute("username").toString());

		String queryString = "";

		if (session.toString() != null) {
			String username = session.getAttribute("username").toString();

			memberSvc.updatePwd(username, pwd);

			session.removeAttribute("username");
		} else {
			queryString = "error";
		}
		return "member/successpwd" + queryString;
	}

	// 비밀번호 변경 성공 페이지로 이동
	@GetMapping("/successpwd")
	public void successForm() {
		log.info("Get - successForm");
	}
	
	//비밀번호 변경
	@PostMapping("/modifypwd")
	public String changePwd(@ModelAttribute("pwd") String pwd, Principal principal) {
		log.info("changePwd : username - {}", pwd);
		
		memberSvc.updatePwd(principal.getName(), pwd);
		
		return "redirect:/memberinfo/profilemodify";
	}

	// 레스트 컨트롤러 모음 ---------------------------------------------------------------
	// 유저네임 중복 체크
	@ResponseBody
	@GetMapping("/checkid")
	public ResponseEntity<String> checkId(@RequestParam(name = "username") String username) {
		log.info("checkId(username = {})", username);
		String result = memberSvc.checkUsername(username);

		return ResponseEntity.ok(result);
	}

	// 닉네임 중복 체크
	@ResponseBody
	@GetMapping("/checknick")
	public ResponseEntity<String> checkNick(@RequestParam(name = "nickname") String nickname) {
		log.info("checkNick(nickname = {})", nickname);
		String result = memberSvc.checkNickname(nickname);

		return ResponseEntity.ok(result);
	}

	// 이메일 중복 체크
	@ResponseBody
	@GetMapping("/checkemail")
	public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
		log.info("checkEmail(email = {})", email);
		String result = "N";

		Member m = memberSvc.checkEmail(email);
		if (m != null) {
			result = "Y";
		}
		return ResponseEntity.ok(result);
	}
	
	//비밀번호 변경시, 현재 비밀번호가 맞는지 확인
	@ResponseBody
	@PostMapping("/checkpwd")
	public ResponseEntity<String> checkPwd(@RequestBody String oldPwd, @AuthenticationPrincipal MemberSecurityDto msd){
		log.info("현재 비밀번호가 맞는지 확인 : oldPwd : {}", oldPwd);
		
		String result = memberSvc.checkPassword(oldPwd, msd.getUserid());
		
		return ResponseEntity.ok(result);
	}
	

}