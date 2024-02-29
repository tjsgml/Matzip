package com.itwill.matzip.repository.member;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.QMember;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.admin.MemberFilterDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

public class MemberQuerydslImpl extends QuerydslRepositorySupport implements MemberQuerydsl {
    public MemberQuerydslImpl() {
        super(Member.class);
    }

    @Override
    public Page<Member> findMemberList(MemberFilterDto filterDto) {
        QMember member = QMember.member;
        JPQLQuery<Member> query = from(member);

        BooleanBuilder bool = new BooleanBuilder();

        switch (filterDto.getRole()) { // 권한
            case "ROLE_USER" -> bool.and(member.roles.contains(MemberRole.USER));
            case "ROLE_ADMIN" -> bool.and(member.roles.contains(MemberRole.ADMIN));
            case "ROLE_GUEST" -> bool.and(member.roles.contains(MemberRole.GUEST));
        }

        switch (filterDto.getSearchCondition()) {
            case "USERNAME" -> bool.and(member.username.containsIgnoreCase(filterDto.getSearchKeyword()));
            case "NICKNAME" -> bool.and(member.nickname.containsIgnoreCase(filterDto.getSearchKeyword()));
            case "EMAIL" -> bool.and(member.email.containsIgnoreCase(filterDto.getSearchKeyword()));
            default ->
                    bool.and(member.email.containsIgnoreCase(filterDto.getSearchKeyword())
                            .or(member.nickname.containsIgnoreCase(filterDto.getSearchKeyword()))
                            .or(member.username.containsIgnoreCase(filterDto.getSearchKeyword()))
                    );
        }

        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getViewCnt());
        query.where(bool);

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        long total = query.fetchCount();

        List<Member> content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
