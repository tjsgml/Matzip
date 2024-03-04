package com.itwill.matzip.repository.member;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.admin.MemberFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberQuerydsl {
    Page<Member> findMemberListByPagination(MemberFilterDto filterDto);

    List<Member> findMemberList(MemberFilterDto filterDto);
}
