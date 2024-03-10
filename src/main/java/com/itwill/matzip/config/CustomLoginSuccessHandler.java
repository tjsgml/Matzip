package com.itwill.matzip.config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("success handle()");

		List<String> roleNames = new ArrayList<>();

		authentication.getAuthorities().forEach((auth) -> {
			roleNames.add(auth.getAuthority());
		});

		if (roleNames.contains("ROLE_GUEST")) {
			response.sendRedirect("/member/addinfo");
			return;
		}
		
		 String redirectUrl = "/";
		 String queryString = request.getQueryString();
		 
		 log.info("queryString={}",queryString);
		// 로그인 후 리다이렉트할 URL이 있으면 변경
        if (request.getParameter("redirect") != null) {
            redirectUrl = request.getParameter("redirect");
            log.info("redirectUrl={}",redirectUrl);
        }
        
        
        // 로그인 후 리다이렉트
        response.sendRedirect(redirectUrl);
		
		
	}
	

}
