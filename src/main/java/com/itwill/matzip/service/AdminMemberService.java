package com.itwill.matzip.service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberFilterDto;
import com.itwill.matzip.dto.NickRespDto;
import com.itwill.matzip.repository.ReviewRepository;
import com.itwill.matzip.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberDao;
    private final ReviewRepository reviewDao;

    private final static Integer REVIEW_LIST_SIZE = 10;

    public Page<Member> getMemberList(MemberFilterDto filterDto) {
        Page<Member> members = memberDao.findMemberList(filterDto);
        log.info("members={}", members);
        return members;
    }

    public Member getMember(Long memberId) {
        return memberDao.findById(memberId).orElseThrow();
    }

    public List<MemberRole> getMemberRoles() {
        return List.of(MemberRole.values());
    }

    public List<Review> getReviewListByMember(Long memberId, Integer curPage) {
        return reviewDao.findByMemberIdOrderByCreatedTime(memberId, PageRequest.of(curPage, REVIEW_LIST_SIZE));
    }
}
