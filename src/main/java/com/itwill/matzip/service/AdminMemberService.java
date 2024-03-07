package com.itwill.matzip.service;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewImage;
import com.itwill.matzip.domain.enums.MemberRole;
import com.itwill.matzip.dto.admin.MemberFilterDto;
import com.itwill.matzip.repository.ReviewImageRepository;
import com.itwill.matzip.repository.review.ReviewRepository;
import com.itwill.matzip.repository.member.MemberRepository;
import com.itwill.matzip.util.S3Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberDao;
    private final ReviewRepository reviewDao;
    private final ReviewImageRepository rlmgDao;
    private final S3Utility s3Util;

    private final static Integer REVIEW_LIST_SIZE = 5;

    public Page<Member> getMemberList(MemberFilterDto filterDto) {
        Page<Member> members = memberDao.findMemberListByPagination(filterDto);
        log.info("members={}", members);
        return members;
    }

    public void deleteReviewImg(Long reviewImg) {
        ReviewImage reviewImage = rlmgDao.findById(reviewImg).orElseThrow();
        s3Util.deleteImageFromS3(reviewImage.getImgUrl());
        rlmgDao.deleteById(reviewImg);
    }

    public Member getMember(Long memberId) {
        return memberDao.findById(memberId).orElseThrow();
    }

    public List<MemberRole> getMemberRoles() {
        return List.of(MemberRole.values());
    }

    public void deleteMember(Long memberId) {
        reviewDao.deleteAllByMemberId(memberId);
        memberDao.deleteById(memberId);
    }

    public Page<Review> getReviewListByMember(Long memberId, Integer curPage) {
        return reviewDao.findByMemberIdOrderByCreatedTime(memberId, PageRequest.of(curPage, REVIEW_LIST_SIZE));
    }

    public void deleteReview(Long reviewId) {
        reviewDao.deleteById(reviewId);
    }

    @Transactional
    public void updateMemberRole(Long memberId, List<MemberRole> roles) {
        Member member = memberDao.findById(memberId).orElseThrow();

        member.clearRoles();
        for (MemberRole role : roles) {
            member.addRole(role);
        }
    }
}
