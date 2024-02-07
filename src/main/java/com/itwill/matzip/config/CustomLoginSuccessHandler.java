package com.itwill.matzip.config;

import java.io.IOException;
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

		response.sendRedirect("/");
	}
}
