package com.itwill.matzip.repository.reviewHashtag;

import com.itwill.matzip.domain.QReviewHashtag;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.dto.admin.HashtagSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ReviewHashtagQuerydslImpl extends QuerydslRepositorySupport implements ReviewHashtagQuerydsl {
    public ReviewHashtagQuerydslImpl() {
        super(ReviewHashtag.class);
    }

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

    ;
}
