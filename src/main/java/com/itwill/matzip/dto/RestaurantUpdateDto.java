package com.itwill.matzip.dto;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.RestaurantStatus;
import lombok.*;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantUpdateDto {

    @NonNull
    private Long restaurantId;

    @NonNull
    private String placeName;

    @NonNull
    private String address;

    @Builder.Default
    private String detailAddress = "";

    @NonNull
    private String contact;

    @NonNull
    private Double lon;

    @NonNull
    private Double lat;

    @NonNull
    private Integer category;

    @NonNull
    private RestaurantStatus status;

    public Category getCategoryToUpdate () {
        return Category.builder()
                .id(category)
                .build();
    }
}
