package com.itwill.matzip.repository.review;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.itwill.matzip.domain.QMember;
import com.itwill.matzip.domain.QReview;
import com.itwill.matzip.domain.QReviewLike;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.dto.MostLikedReviewDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReviewQuerydslImpl extends QuerydslRepositorySupport
	implements ReviewQuerydsl{
	
	public ReviewQuerydslImpl() {
		super(Review.class);
	}
	
	
}
