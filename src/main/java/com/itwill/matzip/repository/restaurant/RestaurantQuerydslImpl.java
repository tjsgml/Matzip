package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.QRestaurant;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.RestaurantSearchCond;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Slf4j
public class RestaurantQuerydslImpl extends QuerydslRepositorySupport implements RestaurantQuerydsl {
    public RestaurantQuerydslImpl() {
        super(Restaurant.class);
    }

    @Override
    public Page<Restaurant> search(RestaurantSearchCond cond) {

        log.info("search(RestaurantSearchCond={})", cond);
        QRestaurant restaurant = QRestaurant.restaurant;
        JPQLQuery<Restaurant> query = from(restaurant);

        BooleanBuilder builder = new BooleanBuilder();

        if (cond.getCategoryCond() != null) {
//            카테고리 조건 있는 경우
            builder.and(restaurant.category.id.eq(cond.getCategoryCond()));
        }

        if (cond.getRestaurantStatus() != null) {
//            운영상태 필터 조건 있는 경우
            builder.and(restaurant.status.eq(cond.getRestaurantStatus()));
        }

        if (cond.getKeyword() != null) {
            switch (cond.getKeywordCriteria()) {
                case "NAME" -> {
                    builder.and(restaurant.name.containsIgnoreCase(cond.getKeyword()));
                }
                case "PLACE" -> {
                    builder.and(builder.or(restaurant.address.containsIgnoreCase(cond.getKeyword()))
                            .or(restaurant.detailAddress.containsIgnoreCase(cond.getKeyword())));
                }
                default -> {
                    builder.and(
                            builder.or(
                                            restaurant.name.containsIgnoreCase(
                                                    cond.getKeyword()))
                                    .or(
                                            restaurant.address.containsIgnoreCase(
                                                    cond.getKeyword()))
                                    .or(
                                            restaurant.detailAddress.containsIgnoreCase(
                                                    cond.getKeyword()))
                    );
                }

            }
        }
        query.where(builder);

        Pageable pageable = null;

        if (cond.getOrder().equals("createdTimeASC")) {
            pageable = PageRequest.of(cond.getCurPage(), cond.getTotalCount(), Sort.Direction.ASC, "createdTime");
        } else if (cond.getOrder().equals("nameASC")) {
            pageable = PageRequest.of(cond.getCurPage(), cond.getTotalCount(), Sort.Direction.ASC, "name");
        } else {
            pageable = PageRequest.of(cond.getCurPage(), cond.getTotalCount(), Sort.Direction.DESC, "createdTime");
        }

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        // 한 페이지에 표시될 데이터(컨텐트)
        List<Restaurant> content = query.fetch();
        // 전체 원소 개수
        long total = query.fetchCount();

        // Page<T> 타입 객체를 생성
        Page<Restaurant> page = new PageImpl<>(content, pageable, total);
        log.info("pageable = {}", page.getNumber());

        return page;
    }
}
