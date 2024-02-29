package com.itwill.matzip.dto.admin;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class CategoryListDto {
    private Integer id;
    private String name;
    private Integer restaurantCnt;

    public static CategoryListDto fromCategory(Category category) {
        List<Restaurant> restaurants = category.getRestaurants();

        return CategoryListDto.builder()
                .id(category.getId())
                .name(category.getName())
                .restaurantCnt(restaurants.size())
                .build();
    }
}
