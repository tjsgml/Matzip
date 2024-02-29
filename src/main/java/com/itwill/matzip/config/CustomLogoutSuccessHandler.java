package com.itwill.matzip.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication auth ) throws IOException,ServletException {
	        String redirectUrl = "/"; // 로그아웃 후 리다이렉트할 페이지(기본은 홈)
	        
	        // 로그아웃 후 리다이렉트할 URL이 있으면 변경
	        if (request.getParameter("redirect") != null) {
	            redirectUrl = request.getParameter("redirect");
	        }
	        
	        
	        // 로그아웃 후 리다이렉트
	        response.sendRedirect(redirectUrl);
	}


}
