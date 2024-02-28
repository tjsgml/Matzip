package com.itwill.matzip.web.admin;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberFilterDto;
import com.itwill.matzip.service.AdminMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired
    AdminMemberService memberService;

    @GetMapping("")
    public String getMemberListPage(Model model) {
        List<MemberRole> memberRoles = memberService.getMemberRoles();
        model.addAttribute("memberRoles", memberRoles);
        return "admin/member-list.html";
    }

    @ResponseBody
    @GetMapping("/list")
    public ResponseEntity<Page<Member>> getMemberList(@ModelAttribute MemberFilterDto filterDto) {
        log.info("hihi");
        log.info("role={}", filterDto);

        Page<Member> memberList = memberService.getMemberList(filterDto);
        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/{memberId}")
    public String getMemberDetail(@PathVariable Long memberId, Model model) {
        Member member = memberService.getMember(memberId);
        model.addAttribute("member", member);

        List<MemberRole> memberRoles = memberService.getMemberRoles();
        model.addAttribute("memberRoles", memberRoles);

        return "admin/detail-member";
    }

    @ResponseBody
    @GetMapping("/{memberId}/review")
    public ResponseEntity<Page<Review>> getReviewListByMember(@PathVariable Long memberId, @RequestParam(name = "curPage", defaultValue = "0") Integer curPage) {
        Page<Review> reviews = memberService.getReviewListByMember(memberId, curPage);

        log.info("reviews={}", reviews.getContent());
        return ResponseEntity.ok(reviews);
    }

    @ResponseBody
    @DeleteMapping("/review/img/{reviewImgId}")
    public ResponseEntity<List<String>> getReviewImgId(@PathVariable("reviewImgId") Long reviewImgId) {
        memberService.deleteReviewImg(reviewImgId);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> getReviewById(@PathVariable("reviewId") Long reviewId) {
        memberService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PatchMapping("/{memberId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable("memberId") Long memberId, @RequestBody List<MemberRole> roles) {
        log.info("roles={}", roles);
        memberService.updateMemberRole(memberId, roles);
        return ResponseEntity.noContent().build();
    }
}
