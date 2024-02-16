package com.itwill.matzip.domain.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum BusinessDay {
    MON ("월"),
    TUE ("화"),
    WED ("수"),
    THU ("목"),
    FRI ("금"),
    SAT ("토"),
    SUN ("일");


    BusinessDay (String kor) {
        this.kor = kor;
    }

    private final String kor;

    public String getKor () {
        return kor;
    }
}
