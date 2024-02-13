package com.itwill.matzip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Restaurant;


public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long>{

		List<BusinessHour> findByRestaurant(Restaurant restaurant);
}
