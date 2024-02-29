package com.itwill.matzip.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberFilterDto {
    @Builder.Default
    private String role = null;
    @Builder.Default
    private String searchCondition = null;
    @Builder.Default
    private String searchKeyword = null;
    @Builder.Default
    private Integer viewCnt = 10;
    @Builder.Default
    private Integer page = 0;
}
