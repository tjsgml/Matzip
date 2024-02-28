package com.itwill.matzip.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum  RestaurantStatus {
    WAIT("승인 대기"),
    OPEN("영업중"),
    CLOSURE("폐업");

    private final String kor;

    public RestaurantStatus getStatus (String status) {
        return RestaurantStatus.valueOf(status);
    }
}