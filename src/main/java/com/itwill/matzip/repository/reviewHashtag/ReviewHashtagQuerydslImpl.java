package com.itwill.matzip.repository.reviewHashtag;

import com.itwill.matzip.domain.QRestaurant;
import com.itwill.matzip.domain.QReviewHashtag;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.dto.admin.HashtagSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import jakarta.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ReviewHashtagQuerydslImpl extends QuerydslRepositorySupport implements ReviewHashtagQuerydsl {
    public ReviewHashtagQuerydslImpl(EntityManager entityManager) {
        super(ReviewHashtag.class);
        this.entityManager = entityManager;
    }

    private final EntityManager entityManager;

    public List<ReviewHashtag> searchReviewHashtagByKeyword(HashtagSearchDto searchDto) {
        QReviewHashtag hashtag = QReviewHashtag.reviewHashtag;
        JPQLQuery<ReviewHashtag> query = from(hashtag);

        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getCategoryId() != null) {
            builder.and(hashtag.htCategory.id.eq(searchDto.getCategoryId()));
        }
        if (searchDto.getKeyword() != null && !searchDto.getKeyword().isBlank()) {
            builder.and(hashtag.keyword.containsIgnoreCase(searchDto.getKeyword()));
        }

        query.where(builder);

        return query.fetch();
    }

    //리뷰 해시태그에서 키워드 검색
    public List<ReviewHashtag> searchByKeyword(String keyword) {
        QReviewHashtag hashtag = QReviewHashtag.reviewHashtag;

        JPQLQuery<ReviewHashtag> query = from(hashtag)
                .where(hashtag.keyword.containsIgnoreCase(keyword));
                
        return query.fetch();
    }

    //카테고리별 키워드 검색
    public List<ReviewHashtag> searchByCategoryAndKeyword(Long categoryId, String keyword) {
    	QReviewHashtag hashtag = QReviewHashtag.reviewHashtag;
        JPQLQuery<ReviewHashtag> query = from(hashtag)
        		.where(hashtag.keyword.containsIgnoreCase(keyword)
        				 .and(hashtag.htCategory.id.eq(categoryId)));

        return query.fetch();
    }

}
