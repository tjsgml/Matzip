package com.itwill.matzip.dto;

import com.itwill.matzip.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantToCreateEntity {

    String placeName;

    String address;

    String detailAddress;

    String contact;

    Double lon;

    Double lat;

    Map<String, BusinessTime> businessTimes;

    public Restaurant toEntity () {
        return Restaurant.builder()
                .name(placeName)
                .address(address)
                .detailAddress(detailAddress)
                .contact(contact)
                .lon(lon)
                .lat(lat)
                .build();
    }
}
