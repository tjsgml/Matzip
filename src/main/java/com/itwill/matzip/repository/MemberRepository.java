package com.itwill.matzip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	//username select
	@EntityGraph(attributePaths = "roles")
	Optional<Member> findByUsername(String username);
	
	Member findByNickname(String nickname);
	
	Member findByEmail(String email);
}
