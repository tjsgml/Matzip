package com.itwill.matzip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessTimePerWeek {
    BusinessTime mon;
    BusinessTime tue;
    BusinessTime wed;
    BusinessTime thu;
    BusinessTime fri;
    BusinessTime sat;
    BusinessTime sun;
}
