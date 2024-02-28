package com.itwill.matzip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwill.matzip.domain.HashtagCategory;
import com.itwill.matzip.domain.ReviewHashtag;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long>{

	// 해시태그 카테고리, 키워드로 중복체크
	Optional<ReviewHashtag> findByKeywordAndHtCategory(String keyword, HashtagCategory htCategory);
	
	@Query("SELECT rh.keyword, hc.name FROM Review r JOIN r.hashtags rh JOIN rh.htCategory hc WHERE r.id = :reviewId")
	List<Object[]> findHashtagsAndCategoriesByReviewId(Long reviewId);

	
}
