package com.itwill.matzip.dto.admin;

import com.itwill.matzip.domain.RestaurantStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSearchCond {

    @Builder.Default
    private Integer categoryCond = null;
    private RestaurantStatus restaurantStatus;
    @Builder.Default
    private String keywordCriteria = "ALL";
    private String keyword;
    @Builder.Default
    private Integer curPage = 0;
    @Builder.Default
    private Integer totalCount = 10;
    @Builder.Default
    private String order = "createdTimeASC";
}
