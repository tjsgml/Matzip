package com.itwill.matzip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.MyPick;

public interface MyPickRepository extends JpaRepository<MyPick, Long>{
	MyPick findByMemberIdAndRestaurantId(Long memberId, Long restaurantId);
	
	//한 restaurant 당 myPick 개수 가져오기.
    List<MyPick> findByRestaurantId(Long restaurantId);

	//member가 가지고 있는 북마크 갯수 가져오기
	Long countAllByMemberId(Long memberid);
	
	//member가 가지고 있는 북마크 모두 가져오기
	List<MyPick> findByMemberId(Long memberid);

}
