package com.itwill.matzip.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{
	//username select
	@EntityGraph(attributePaths = "roles")
	Optional<Member> findByUsername(String username);
}
