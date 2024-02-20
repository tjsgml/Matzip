package com.itwill.matzip.dto;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.enums.BusinessDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHourUpdateDto {
    private Long bhourId;
    Boolean isHoliday;
    Integer startHour;
    Integer startMinute;
    Integer endHour;
    Integer endMinute;
    String day;

    public String getStartTime() {

        if (startHour == null || startMinute == null) {
            return null;
        }

        return String.format("%02d", startHour) + ":" + String.format("%02d", startMinute);
    }

    public String getEndTime() {
        if (endHour == null || endMinute == null) {
            return null;
        }

        return String.format("%02d", endHour) + ":" + String.format("%02d", endMinute);
    }


    public BusinessHour toBusinessHour(Restaurant restaurant) {
        return BusinessHour.builder()
                .id(bhourId != null ? bhourId : null)
                .days(day != null ? BusinessDay.valueOf(day) : null)
                .isHoliday(isHoliday)
                .openTime(getStartTime())
                .closeTime(getEndTime())
                .restaurant(restaurant)
                .build();
    }

    public BusinessHour toBusinessHour() {
        return toBusinessHour(null);
    }
}