package com.itwill.matzip.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydsl {
    //username select
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByUsername(String username);

    Optional<Member> findBykakaoClientId(String id);

    Member findByNickname(String nickname);

    Member findByEmail(String email);
}
