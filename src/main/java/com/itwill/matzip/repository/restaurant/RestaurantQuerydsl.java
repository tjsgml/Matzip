package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.RestaurantSearchCond;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RestaurantQuerydsl {
    Page<Restaurant> search (RestaurantSearchCond cond); // 검색 조건 담을 Dto 생성 필요
}
