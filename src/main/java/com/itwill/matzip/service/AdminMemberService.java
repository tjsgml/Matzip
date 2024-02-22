package com.itwill.matzip.service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.MemberFilterDto;
import com.itwill.matzip.dto.NickRespDto;
import com.itwill.matzip.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AdminMemberService {
    @Autowired
    private MemberRepository memberDao;

    public Page<Member> getMemberList(MemberFilterDto filterDto) {
        Page<Member> members = memberDao.findMemberList(filterDto);
        log.info("members={}", members);
        return members;
    }

    public List<MemberRole> getMemberRoles () {
        return List.of(MemberRole.values());
    }
}
