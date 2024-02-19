package com.itwill.matzip.dto;

import com.itwill.matzip.domain.enums.BusinessDay;

public class businessHourUpdateDto {
    private Long bhourId;
    private BusinessDay bhourDay;
    private Boolean isHoliday;
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;


}
