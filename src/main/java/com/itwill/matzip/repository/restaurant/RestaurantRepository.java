package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantQuerydsl {
	
	List<Restaurant> findAllByStatus(RestaurantStatus status);
}
