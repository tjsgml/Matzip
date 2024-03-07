package com.itwill.matzip.repository.review;

import com.itwill.matzip.domain.Review;
import org.springframework.data.domain.Page;

public interface ReviewQuerydsl {

    public Page<Review> getReviewsByRestaurantIdPerPage (Long restaurantId, Integer page);

}
