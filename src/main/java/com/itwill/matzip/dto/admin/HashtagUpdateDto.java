package com.itwill.matzip.dto.admin;


import com.itwill.matzip.domain.enums.Expose;
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
    private Expose expose = Expose.N;
}
