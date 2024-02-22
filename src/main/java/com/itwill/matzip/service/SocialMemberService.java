package com.itwill.matzip.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberSecurityDto;
import com.itwill.matzip.dto.MemberUpdateRequestDto;
import com.itwill.matzip.repository.member.MemberRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocialMemberService extends DefaultOAuth2UserService {

	private final MemberRepository memberDao;
	private final PasswordEncoder passwordEncoder;
	private final MemberService memSvc;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("OAuth - loadUserByUsername(userRequest = {}", userRequest);

		// 카카오 계정 정보 가져오기
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 카카오 계정 정보(username, nickname, profile_img) String 으로 변환
		Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

		String kakaoUsername = oAuth2User.getAttributes().get("id").toString();
		String kakaoNickname = (String) profile.get("nickname");
		String kakaoProfileImageUrl = (String) profile.get("profile_image_url");
		log.info("user information : username {} , nickname : {}, img : {}", kakaoUsername, kakaoNickname,
				kakaoProfileImageUrl);

		// username 으로 로그인 확인
		Optional<Member> opt = memberDao.findBykakaoClientId(kakaoUsername);

		if (!opt.isPresent()) { // 없으면 저장
			Member entity = Member.builder().username("kakao_" + kakaoUsername)
					.password(passwordEncoder.encode(UUID.randomUUID().toString())).nickname(kakaoNickname)
					.kakaoClientId(kakaoUsername).img(kakaoProfileImageUrl).build();
			entity.addRole(MemberRole.GUEST);
			memberDao.save(entity);

			return MemberSecurityDto.fromEntity(entity);
		} else {
			return MemberSecurityDto.fromEntity(opt.get());
		}
	}

	// 카카오 로그인 추가 정보 업데이트 및 권한 수정
	@Transactional
	public void updateMember(MemberUpdateRequestDto dto, HttpSession session) {
		log.info("Svc - updateMember(dto = {})", dto);

		Member entity = memberDao.findByUsername(dto.getUsername()).orElseThrow();
		entity.socialMemUpdate(dto.toEntity());
		entity.clearRoles();
		entity.addRole(MemberRole.USER);

		sessionReset(entity);
	}

	// 로그인 상태의 유저 세션 정보를 변경 - 권한 변경해줌
	public void sessionReset(Member member) {
		log.info("sessionReset : {}", member);
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (MemberRole role : member.getRoles()) {
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role.getAuthority());
			authorities.add(auth);
		}

		UserDetails user = memSvc.loadUserByUsername(member.getUsername());
		Authentication newAuthentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

		log.info("newAuthentication : {} ", newAuthentication);

		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
}
