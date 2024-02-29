package com.itwill.matzip.dto.admin;


import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.enums.BusinessDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessTimeDto {
    Boolean isHoliday;
    Integer startHour;
    Integer startMinute;
    Integer endHour;
    Integer endMinute;
    String day;

    public String getStartTime() {
        return String.format("%02d", startHour) + ":" + String.format("%02d", startMinute);}

    public String getEndTime() {
        return String.format("%02d", endHour) + ":" + String.format("%02d", endMinute);
    }

    public BusinessHour toEntity(Restaurant restaurant) {
        if (!isHoliday) {
            return BusinessHour.builder()
                    .restaurant(restaurant)
                    .isHoliday(isHoliday)
                    .openTime(getStartTime())
                    .closeTime(getEndTime())
                    .days(BusinessDay.valueOf(day.trim()))
                    .build();
        } else {
            return BusinessHour.builder()
                    .isHoliday(isHoliday)
                    .restaurant(restaurant)
                    .days(BusinessDay.valueOf(day.trim()))
                    .build();
        }
    }

}
