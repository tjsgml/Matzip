package com.itwill.matzip.dto;

import com.itwill.matzip.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantToCreateEntity {

    String placeName;

    String address;

    String detailAddress;

    String contact;

    Double lng;

    Double lat;

    BusinessTimePerWeek businessTime;

    public Restaurant toEntity () {
        return Restaurant.builder()
                .name(placeName)
                .address(address)
                .detailAddress(detailAddress)
                .contact(contact)
                .lon(lng)
                .lat(lat)
                .build();
    }
}
