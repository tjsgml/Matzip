package com.itwill.matzip.repository.member;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.dto.admin.MemberFilterDto;
import org.springframework.data.domain.Page;

public interface MemberQuerydsl {
    Page<Member> findMemberList (MemberFilterDto filterDto);

}
