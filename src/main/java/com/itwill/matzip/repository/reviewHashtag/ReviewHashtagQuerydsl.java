package com.itwill.matzip.repository.reviewHashtag;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.dto.admin.HashtagSearchDto;

import java.util.List;

public interface ReviewHashtagQuerydsl {
    List<ReviewHashtag> searchReviewHashtagByKeyword(HashtagSearchDto searchDto);
    
    //---선희 ---
    List<ReviewHashtag> searchByKeyword(String keyword);
    
   List<ReviewHashtag> searchByCategoryAndKeyword(Long categoryId, String keyword);
    //------------
}
