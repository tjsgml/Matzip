package com.itwill.matzip.web.admin;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberFilterDto;
import com.itwill.matzip.service.AdminMemberService;
import com.itwill.matzip.service.AdminService;
import com.itwill.matzip.service.MemberInfoService;
import lombok.Builder;
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

}
