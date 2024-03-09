package com.itwill.matzip.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
	
	 List<ReviewLike> findByReviewId(Long reviewId);
	 
	 int countAllByReviewId(Long reviewId);

	 Optional<ReviewLike> findByMemberIdAndReviewId(Long memberId, Long reviewId);

	 boolean existsByReviewIdAndMemberId(Long reviewId, Long memberId);

	Long countByReviewId(Long reviewId);
	 





}
