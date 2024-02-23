package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantQuerydsl {
	/*t선희*/
	//오픈 중인 모든 음식점 가져오기
	List<Restaurant> findAllByStatus(RestaurantStatus status);
	//카테고리 별 음식점 가져오기
	@Query("SELECT r FROM Restaurant r WHERE r.status = :status AND r.category.id = :categoryId")
	List<Restaurant> findByStatusAndCategoryId(@Param("status") RestaurantStatus status, @Param("categoryId") Integer categoryId);
	//전체 검색 결과
	List<Restaurant> searchAllByKeyword(String keyword);
	//카테고리별 검색 결과
	List<Restaurant> searchByCategoryAndKeyword(Integer categoryId, String keyword);
}
