package com.itwill.matzip.repository.review;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.*;
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
        implements ReviewQuerydsl {

    public ReviewQuerydslImpl() {
        super(Review.class);
    }


    @Override
    public Page<Review> getReviewsByRestaurantIdPerPage(Long restaurantId, Integer page) {

        QReview review = QReview.review;
        JPQLQuery<Review> query = from(review);

        BooleanBuilder builder = new BooleanBuilder();
        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "id");

        builder.and(review.restaurant.id.eq(restaurantId));
        query.where(builder);

        List<Review> reviewList = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(reviewList, pageable, total);
    }
}
