package com.itwill.matzip.dto;

import com.itwill.matzip.domain.Category;
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

    public Category getCategoryToUpdate () {
        return Category.builder()
                .id(category)
                .build();
    }
}
