package com.itwill.matzip.repository.reviewHashtag;

import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.dto.admin.HashtagSearchDto;

import java.util.List;

public interface ReviewHashtagQuerydsl {
    List<ReviewHashtag> searchReviewHashtagByKeyword(HashtagSearchDto searchDto);
}
