package com.itwill.matzip.web;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.*;
import com.itwill.matzip.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/memberinfo")
@Controller
public class MemberInfoController {

	private final MemberInfoService miSvc;
	private final RestaurantService resSvc;

	@GetMapping("/mymain")
	public void myMain(Principal principal, Model model) {
		log.info("마이 페이지 [마이픽]");
		String username = principal.getName().toString();
		Member member = miSvc.getMember(username);

		// 헤더에 있는 프로필 정보 가져옴
		MemberMainHeaderRequestDto dto = miSvc.getProfileInfo(member);

		// 북마크한 레스토랑 리스트 가져오기
		List<MyPickRestaurantRequestDto> pickList = miSvc.getMyPickRestaurant(member);

		model.addAttribute("info", dto);
		model.addAttribute("mypicks", pickList);
	}

	@GetMapping("/myreview")
	public void myReview(Principal principal, Model model) {
		log.info("마이 페이지 [리뷰]");
		String username = principal.getName().toString();
		Member member = miSvc.getMember(username);

		// 헤더에 있는 프로필 정보 가져옴
		MemberMainHeaderRequestDto dto = miSvc.getProfileInfo(member);
		
		//내가 쓴 리뷰 리스트 가져오기
		List<MyReviewRequestDto> reviewlist = miSvc.getReviews(member);
		
		model.addAttribute("info", dto);
		model.addAttribute("reviews",reviewlist);
	}

	@GetMapping("/profilemodify")
	public void profileModify(Principal principal, Model model) {
		log.info("마이 페이지 [프로필 수정]");
		
		String username = principal.getName().toString();
		Member member = miSvc.getMember(username);
		
		model.addAttribute("memberinfo", member);
	}

	@GetMapping("/pwdmodify")
	public void pwdmodify() {
		log.info("마이페이지 [비밀번호 수정]");
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") Long pickId) {
		log.info("북마크 해제하기 : restaurantId - {}", pickId);

		resSvc.deleteMyPick(pickId);

		return "redirect:/memberinfo/mymain";
	}
	
	@ResponseBody
	@GetMapping("changeDefaultImg/{imgFile}")
	public ResponseEntity<String> changeDefaultImg(@PathVariable("imgFile")MultipartFile file,Principal principal){
		log.info("프로필 이미지 변경");
		
		if(file.isEmpty()) {
			String result = miSvc.changeProfileImg(principal.getName());
		}else {
			
		}
		
		return ResponseEntity.ok(null);
	}
}
