package com.itwill.matzip.repository.reviewHashtag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.HashtagCategory;
import com.itwill.matzip.domain.ReviewHashtag;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long> , ReviewHashtagQuerydsl{

	// 해시태그 카테고리, 키워드로 중복체크
	Optional<ReviewHashtag> findByKeywordAndHtCategory(String keyword, HashtagCategory htCategory);
	
}
