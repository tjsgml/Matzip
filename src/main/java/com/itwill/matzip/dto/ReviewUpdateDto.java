package com.itwill.matzip.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewUpdateDto {
	
	private int tasteRating;
    private int priceRating;
    private int serviceRating;
    private String reviewContent;
    private List<String> visitPurposeTags;
    private List<String> moodTags;
    private List<String> convenienceTags;
    private MultipartFile[] images;
    private Long restaurantId;
    private Long memberId;
    private List<String> deleteImageUrls; // 삭제 요청된 이미지 URL 목록
    private List<Long> deleteHashtagIds; // 삭제 요청된 해시태그 ID 목록
    
    
    
}
