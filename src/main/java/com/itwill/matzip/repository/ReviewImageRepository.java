package com.itwill.matzip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

	//이미지 배열 가져오기
	List<ReviewImage> findByReviewId(Long reviewId);

}
