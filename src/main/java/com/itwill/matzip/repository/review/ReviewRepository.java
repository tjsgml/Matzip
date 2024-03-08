package com.itwill.matzip.repository.review;

import java.util.List;

import com.itwill.matzip.domain.HashtagCategory;
import com.itwill.matzip.domain.ReviewHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwill.matzip.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQuerydsl {

    //멤버 아이디에 대한 총 리뷰 수 구하기
    Long countAllByMemberId(Long memberid);

    //레스토랑 아이디에 대한 리뷰 가져오기
    List<Review> findByRestaurantId(Long restaurantId);

    //멤버 아이디에 대한 리뷰 리스트 구하기
    List<Review> findByMemberIdOrderById(Long userId);

    Page<Review> findByMemberIdOrderByCreatedTime(Long userId, Pageable pageable);

    List<Review> findAllByRestaurantId(Long restaurantId);

    Review findReviewsByHashtagsId(Long hashtags_id);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.hashtags WHERE r.restaurant.id = :restaurantId")
    List<Review> findByRestaurantIdWithHashtags(@Param("restaurantId") Long restaurantId);

    void deleteAllByMemberId(Long userId);
}
