package com.itwill.matzip.MemberRepo;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.opaqueToken;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwill.matzip.domain.Gender;
import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.MemberRepository;
import com.itwill.matzip.domain.MemberRole;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberRepositoryTest {
	@Autowired private MemberRepository memberDao;
	@Autowired private PasswordEncoder passwordEncoder;
	
	//@Test
	public void beanTest() {
		Assertions.assertNotNull(memberDao);
		Assertions.assertNotNull(passwordEncoder);
	}
	
	//@Test
	public void InsertMemberTest() {
		Member m = Member.builder()
									.username("username1")
									.password(passwordEncoder.encode("1111"))
									.email("user1@naver.com")
									.birth(LocalDate.of(1993,7 , 20))
									.nickname("ys")
									.gender(Gender.Female)
									.build();
		m.addRole(MemberRole.ADMIN);
		
		log.info("save 전 : {} ", m );
		
		memberDao.save(m);
		
		log.info("save 후 : {}", m);
	}
	
	//@Test @Transactional
	public void findAllTest() {
		List<Member> list = memberDao.findAll();
		list.forEach((x) -> log.info("{}, {}", x, x.getRoles()));
	}
	
	@Test
	public void findByUsernameTest() {
		Optional<Member> m = memberDao.findByUsername("username1");
		log.info("{}, {}", m.get(), m.get().getRoles());
	}
	
}
