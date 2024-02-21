package com.itwill.matzip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwill.matzip.domain.Review;
import com.itwill.matzip.dto.MapReviewDto;
import com.itwill.matzip.dto.MostLikedReviewDto;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByRestaurantId(Long id);

}
