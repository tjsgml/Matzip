package com.itwill.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
