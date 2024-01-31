package com.itwill.matzip.oauth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.matzip.domain.OauthToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/login")
@Controller
public class KakaoController {
	
	//카카오 로그인 페이지 이동
	@GetMapping("/kakao")
	public String loginKaKao() {
		log.info("Get - Kakao form 이동");
		
		return "redirect:https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=b1e1c4f27362d346d8dff0189b01c048&redirect_uri=http://localhost:8081/login/oauth2/code/kakao";
	}
	
	//카카오 로그인 인증 코드
	@ResponseBody
	@GetMapping("/oauth2/code/kakao")
	public ResponseEntity<String> loginCallback(@RequestParam("code") String code){
		log.info("Get - kakao 인증");
		
		//AccessToken 만 가져오기
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "b1e1c4f27362d346d8dff0189b01c048");
		params.add("redirect_uri", "http://localhost:8081/login/oauth2/code/kakao");
		params.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
				HttpMethod.POST, // 요청할 방식
				kakaoTokenRequest, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);
		
		ObjectMapper objMapper = new ObjectMapper();
		OauthToken oauthToken = null;
		
		try {
			oauthToken = objMapper.readValue(response.getBody(), OauthToken.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
		
		
		//사용자 정보 가져오기
		RestTemplate rt2 = new RestTemplate();
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(headers2);
		
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
				HttpMethod.POST, // 요청할 방식
				kakaoRequest, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);
		
		System.out.println("response2 : " + response2.getBody());
		
		return ResponseEntity.ok("카카오 토큰 바디 : "+response.getBody());
	}
	

}
