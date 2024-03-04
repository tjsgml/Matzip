package com.itwill.matzip.dto.admin;

import com.itwill.matzip.domain.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequestDto {
    @Builder.Default
    private ApprovalStatus status = null;
    @Builder.Default
    private String keyword = "";
    @Builder.Default
    private Integer page = 0;
}
