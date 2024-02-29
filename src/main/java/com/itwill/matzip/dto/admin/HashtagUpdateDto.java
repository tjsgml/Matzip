package com.itwill.matzip.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashtagUpdateDto {
    private String tagName;
    private Long categoryId;
}
