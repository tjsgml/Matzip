package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.admin.RestaurantSearchCond;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RestaurantQuerydsl {
	List<Restaurant> search(RestaurantSearchCond cond);
	Page<Restaurant> searchByPagination(RestaurantSearchCond cond); // 검색 조건 담을 Dto 생성 필요
    void updateCategoryToDefaultCategory(Integer categoryId, Integer categoryIdToChange);
    
	List<Restaurant> searchAllByKeyword(String keyword);
	
	//카테고리별 검색 결과
	List<Restaurant> searchByCategoryAndKeyword(Integer categoryId, String keyword);
}
