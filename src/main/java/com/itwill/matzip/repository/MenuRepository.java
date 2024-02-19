package com.itwill.matzip.repository;

import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findByRestaurant(Restaurant rest);

	void deleteAllByRestaurantId (Long restaurantId);
}
