package com.itwill.matzip.dto;

import java.util.HashSet;
import java.util.Set;

import com.itwill.matzip.domain.ReviewHashtag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchListDto {
	//restaurant 관련
	private Long restId;
	private String restName;
	private String restCategory;
	private double restLat;
	private double restLon;
	//myPick 관련
	private int myPickTotal;//좋아요 개수
	//review 관련
	
	private double rvTotalRating;//리뷰 평점
	private int rvTotal;//리뷰 개수
	private Long rvId;//베스트 리뷰 아이디(공감수를 가장 많이 받은 리뷰)
	private String rvContent;
	private String rvNickname;
	private String rvImg;
	private int rvLikes;
	private Set<String> rvHashtags = new HashSet<>();
	
	//ReviewLike 관련
	private Long rvLikeTotal;//베스트 리뷰에 관한 공감수
	
	
}
