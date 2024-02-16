package com.itwill.matzip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.MyPick;

public interface MyPickRepository extends JpaRepository<MyPick, Long>{
	MyPick findByMemberIdAndRestaurantId(Long memberId, Long restaurantId);
}
