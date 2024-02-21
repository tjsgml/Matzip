package com.itwill.matzip.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.MemberSignupRequestDto;
import com.itwill.matzip.dto.MemberSocialUpdateDto;
import com.itwill.matzip.service.MailService;
import com.itwill.matzip.service.MemberService;
import com.itwill.matzip.service.SocialMemberService;

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
	public void login() {
		log.info("Get - login()");
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

	// 유저네임 중복 체크
	@GetMapping("/checkid")
	@ResponseBody
	public ResponseEntity<String> checkId(@RequestParam(name = "username") String username) {
		log.info("checkId(username = {})", username);

		String result = memberSvc.checkUsername(username);

		return ResponseEntity.ok(result);
	}

	// 닉네임 중복 체크
	@GetMapping("/checknick")
	@ResponseBody
	public ResponseEntity<String> checkNick(@RequestParam(name = "nickname") String nickname) {
		log.info("checkNick(nickname = {})", nickname);

		String result = memberSvc.checkNickname(nickname);

		return ResponseEntity.ok(result);
	}

	// 이메일 중복 체크
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

	// 소셜 로그인 회원 추가 정보 입력 폼으로 이동
	@PreAuthorize("hasRole('GUEST')")
	@GetMapping("/addinfo")
	public void addinfo() {
		log.info("추가 정보 창 띄움");
	}

	// 소셜 로그인 회원 추가 정보 저장
	@PostMapping("/addinfo")
	public String addinfo(MemberSocialUpdateDto dto, HttpSession session) {
		log.info("addInfo(dto : {})", dto);

		socialSvc.updateMember(dto, session);

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

		if (m != null) {
			// 이메일에 해당하는 유저 존재
			reAtt.addFlashAttribute("email", m.getEmail());
			session.setAttribute("username", m.getUsername());

			mailService.sendHtmlEmail(m);

			return "redirect:/member/forgot";
		} else {
			// 이메일에 해당하는 유저 존재하지 않음
			return "redirect:/member/forgot?error";
		}
	}

	// 비밀번호 변경 폼으로 이동
	@GetMapping("/password")
	public String passwordForm(@RequestParam(required = false, name="key") String key, HttpSession session, Model model) {
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

		return "/member/password";
	}

	// 비밀번호 변경하기
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
		return "/member/successpwd" + queryString;
	}

	// 비밀번호 변경 성공 페이지로 이동
	@GetMapping("/successpwd")
	public void successForm() {
		log.info("Get - successForm");

	}
}
