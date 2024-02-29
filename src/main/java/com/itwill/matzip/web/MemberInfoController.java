package com.itwill.matzip.web;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.dto.*;
import com.itwill.matzip.repository.member.MemberRepository;
import com.itwill.matzip.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/memberinfo")
@Controller
public class MemberInfoController {

	private final RestaurantService resSvc;
	private final ReviewService reviewSvc;
	private final MemberService memSvc;
	private final MemberRepository mdao;
	
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/mymain")
	public void myMainForm(@AuthenticationPrincipal MemberSecurityDto msd, Principal principal,Model model) {
		log.info("마이 페이지 [마이픽]");

		// 헤더에 있는 프로필 정보 가져옴
		MemberMainHeaderRequestDto dto = memSvc.getProfileInfo(msd);
		model.addAttribute("info", dto);
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/myreview")
	public void myReviewForm(@AuthenticationPrincipal MemberSecurityDto msd, Model model) {

		// 헤더에 있는 프로필 정보 가져옴
		MemberMainHeaderRequestDto dto = memSvc.getProfileInfo(msd);
		model.addAttribute("info", dto);
		
		
		//내 리뷰 총 평점 구하기 TODO :
		List<Review> reviewlist = reviewSvc.getAllReviews(msd.getUserid());
		Double totalReviewRating = reviewSvc.getTotalRating(reviewlist);
		model.addAttribute("totalRating", totalReviewRating);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profilemodify")
	public void profileModify(Principal principal, Model model) {
		log.info("마이 페이지 [프로필 수정]");

		String username = principal.getName().toString();
		Member member = mdao.findByUsername(username).orElse(null);

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

	//레스트 컨트롤러 ---------------------------------------------------
	// 북마크한 레스토랑 리스트 가져오기
	// 마이픽 페이지 처리함 - 무한 스크롤 이용시 값만 전달
	@GetMapping("/api/mypick")
	public ResponseEntity<Page<MyPickRequestDto>> myMain(@AuthenticationPrincipal MemberSecurityDto msd, Model model, @RequestParam(name="p") int p) {
		log.info("마이 페이지 [북마크] : p - {}", p);

		Page<MyPickRequestDto> pickList = resSvc.getMyPickRestaurant(msd.getUserid(), p);
		
		return ResponseEntity.ok(pickList);
	}
	
	
	//내가 쓴 리뷰 리스트 가져옴
	//리뷰 페이지 처리함
	@ResponseBody
	@GetMapping("/api/myreview")
	public ResponseEntity<Page<MyReviewRequestDto>> myReviewApi(@AuthenticationPrincipal MemberSecurityDto msd, Model model, @RequestParam(name = "p") int p) {
		log.info("마이 페이지 [리뷰] : p - {}", p);

		Page<MyReviewRequestDto> reviewlist = reviewSvc.getReviews(msd.getUserid(), p);

		return ResponseEntity.ok(reviewlist);
	}
	
	
	// 기본 이미지로 프로필 변경
	@ResponseBody
	@GetMapping("changeDefaultImg")
	public ResponseEntity<String> changeDefaultImg(@AuthenticationPrincipal MemberSecurityDto msd) {
		log.info("기본 프로필 이미지 변경");

		String result = memSvc.changeProfileDefaultImg(msd.getUserid());

		return ResponseEntity.ok(result);
	}

	// 커스텀 프로필 변경
	@ResponseBody
	@PostMapping("/changeCtmImg")
	public ResponseEntity<String> changeCtmImg(@RequestParam("file") MultipartFile imgFile, @AuthenticationPrincipal MemberSecurityDto msd) {
		log.info("커스텀 프로필 이미지 변경 : file - {}", imgFile);

		String result = memSvc.changeProfileCtmImg(msd.getUserid(), imgFile);

		return ResponseEntity.ok(result);
	}
}
