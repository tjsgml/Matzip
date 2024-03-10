package com.itwill.matzip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	//비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	//스프링 시큐리티 필터 체인
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable());
		
		//로그인
		http.formLogin((login) -> login.loginPage("/member/login").successHandler(new CustomLoginSuccessHandler()));
		//소셜 로그인
		http.oauth2Login((login)->login.loginPage("/member/login").successHandler(new CustomLoginSuccessHandler()));
		
		//로그아웃
		http.logout((logout) -> logout.logoutSuccessHandler(new CustomLogoutSuccessHandler()));
		
		return http.build();
	}
}
