package com.itwill.matzip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;

public interface MenuRepository extends JpaRepository<Menu, Long>{
	
	List<Menu> findByRestaurant(Restaurant rest);
}
