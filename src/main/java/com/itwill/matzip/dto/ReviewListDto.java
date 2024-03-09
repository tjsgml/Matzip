package com.itwill.matzip.dto;

import java.util.List;
import java.util.Set;


import com.itwill.matzip.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewListDto {
    private Long id;
    private String content;
    private Integer flavorScore;
    private Integer serviceScore;
    private Integer priceScore;
    private String formattedRegisterDate; // reviewRegisterDate DateTimeUtil사용할거라 String 타입으로..
    private String memberNickname;
    private String memberImg;
    private List<String> reviewImages;
    private Set<String> hashtags;
    private Set<String> hts;// 카테고리로 검색시 보여줄 결과 카테고리들..
    private boolean likedByUser;
    private Long likesCount; // 리뷰 좋아요 개수
}
