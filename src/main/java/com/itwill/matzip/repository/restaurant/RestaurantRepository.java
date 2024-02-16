package com.itwill.matzip.repository.restaurant;

import com.itwill.matzip.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantQuerydsl {
}
