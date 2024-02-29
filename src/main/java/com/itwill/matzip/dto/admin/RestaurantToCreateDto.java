package com.itwill.matzip.dto.admin;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.admin.BusinessTimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantToCreateDto {

    String placeName;

    String address;

    String detailAddress;

    String contact;

    Double lon;

    Double lat;

    Integer category;

    Map<String, BusinessTimeDto> businessTimes;

    public Restaurant toEntity () {
        return Restaurant.builder()
                .name(placeName)
                .address(address)
                .detailAddress(detailAddress)
                .contact(contact)
                .lon(lon)
                .lat(lat)
                .category(Category.builder()
                        .id(category)
                        .build())
                .build();
    }
}
