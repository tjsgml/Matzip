package com.itwill.matzip.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessTime {
    Boolean isHoliday;
    Integer startHour;
    Integer startMinute;
    Integer endHour;
    Integer endMinute;

    public String getStartTime () {
        return startHour + "시 " + startMinute +"분";
    }

    public String getEndTime () {
        return endHour + "시 " + endMinute +"분";
    }
}
