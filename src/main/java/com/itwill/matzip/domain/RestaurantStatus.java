package com.itwill.matzip.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RestaurantStatus {
    WAIT,
    OPEN,
    CLOSURE;

    public RestaurantStatus getStatus (String status) {
        return RestaurantStatus.valueOf(status);
    }
}