package com.itwill.matzip.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.service.MemberService;

public class SecurityUtility {

    private static MemberService memberService;

    public SecurityUtility(MemberService memberService) {
        SecurityUtility.memberService = memberService;
    }

    /**
     * 현재 인증된 사용자 username 반환.
     * 
     * @return 현재 인증된 사용자 username, 인증되지 않았으면 null 반환.
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }


}
