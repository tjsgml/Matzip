package com.itwill.matzip.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberSecurityDto;
import com.itwill.matzip.dto.MemberSignupRequestDto;
import com.itwill.matzip.repository.member.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

	private final MemberRepository memberDao;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.info("loadUserByUsername(username={})", username);

		Optional<Member> opt = memberDao.findByUsername(username);

		if (opt.isPresent()) {
			log.info("나 실행 : {}",opt.toString());
			return MemberSecurityDto.fromEntity(opt.get());
		} else {
			throw new UsernameNotFoundException(username + " 찾을 수 없음");
		}
	}

	// 유저 아이디 중복 검사
	public String checkUsername(String username) {
		log.info("checkUsername(username = {})", username);

		Optional<Member> opt = memberDao.findByUsername(username);
		if (opt.isPresent()) {
			log.info(opt.toString());
			return "Y";
		} else {
			return "N";
		}
	}

	// 닉네임 중복 검사
	public String checkNickname(String nickname) {
		log.info("checkNickname(nickname = {})", nickname);

		Member m = memberDao.findByNickname(nickname);

		if (m != null) {
			return "Y";
		} else {
			return "N";
		}
	}

	// 이메일 중복 검사
	public Member checkEmail(String email) {
		log.info("checkEmail(email = {})", email);

		Member m = memberDao.findByEmail(email);

		return m;
	}

	// 이메일 인증키와 username이 맞는지 검사
	public boolean authKey(String key, String username) {
		boolean result = passwordEncoder.matches(username, key);

		return result;
	}
	
	//입력한 비밀번호가 현재 비밀번호와 같은지 확인
	public String checkPassword(String oldPwd, String username) {
		log.info("memberSvc : oldPwd - {}", oldPwd);
		String strResult = "N";
		
		Member m = memberDao.findByUsername(username).orElse(null);
		
		boolean result = passwordEncoder.matches(oldPwd, m.getPassword());
		if(result) {
			//입력된 비밀번호가 현재 비밀번호와 같음
			strResult = "Y";
		}
		
		return strResult;
	}

	// 정보 삽입/수정 메서드 모음 -------------------------------------------------------
	// 회원가입
	public void createMember(MemberSignupRequestDto dto) {
		log.info("createMember : {}", dto);

		Member entity = dto.toEntity(passwordEncoder);
		entity.addRole(MemberRole.USER);

		memberDao.save(entity);
	}

	// 비밀번호 변경
	@Transactional
	public void updatePwd(String username, String pwd) {
		log.info("updatePwd(username : {}, pwd : {}", username, pwd);

		Member entity = memberDao.findByUsername(username).orElse(null);
		entity.pwdUpdate(passwordEncoder.encode(pwd));
	}
}
