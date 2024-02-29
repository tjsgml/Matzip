package com.itwill.matzip.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashtagSearchDto {
    @Builder.Default
    private Long categoryId = null;
    @Builder.Default
    private String keyword = null;
}
