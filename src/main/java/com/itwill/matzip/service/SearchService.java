package com.itwill.matzip.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.domain.ReviewLike;
import com.itwill.matzip.dto.SearchListDto;
import com.itwill.matzip.repository.MyPickRepository;
import com.itwill.matzip.repository.ReviewLikeRepository;
import com.itwill.matzip.repository.review.ReviewRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import com.itwill.matzip.repository.reviewHashtag.ReviewHashtagRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
	//RESTAURANT 테이블
	private final RestaurantRepository restDao;
    //REVIEW 테이블
    private final ReviewRepository rvDao;
    //REVIEWLIKE 테이블
    private final ReviewLikeRepository reviewLikeDao;
    //MYPICK 테이블
    private final MyPickRepository myPickDao;
    //REVIEW_HASHTAG 테이블
    private final ReviewHashtagRepository reviewHTDao;
    
    //리뷰 서비스 - 리뷰에 있는 총점 메서드 사용하기 위함
    private final ReviewService reviewSvc;
    
	//전체 검색(가게명,방문목적,분위기,편의시설)
	public List<SearchListDto> searchAll(String keyword){
		
		//전체 dto 저장할 list
		List<SearchListDto> list = new ArrayList<SearchListDto>();
		
		//겹치지 않게 최종으로 저장할 list
		Map<Long, SearchListDto> resultMap = new LinkedHashMap<Long, SearchListDto>();
			
		//1. 레스토랑 이름에서 검색
		List<Restaurant> rests = restDao.searchAllByKeyword(keyword);
		
		for(Restaurant rest : rests) {//한 가게에 대하여
			
			SearchListDto dto = turnSearchList(rest);
			
			list.add(dto);
			
			log.info("@@@@ 가게명으로 검색 list의 restaurant 아이디={}",dto.getRestId());
			
		}
		
		//2.해시태그에서 검색
		
		Set<Restaurant> openRestaurants = new HashSet<>();//"OPEN" 상태인 가게들 리스트.
		
		List<ReviewHashtag> list_HT = reviewHTDao.searchByKeyword(keyword);
		log.info("@@@ 해시태그 검색 결과 리뷰 수={}",list_HT.size());
		if(list_HT != null) {
			for(ReviewHashtag tag : list_HT) {
				log.info("@@@@ 해시태그 이름={}",tag.getKeyword());
				for(Review review : tag.getReviews()) {
					Restaurant rest = review.getRestaurant();//해시태그가 들어간 음식점을 가져옴
					if (rest.getStatus() == RestaurantStatus.OPEN) {//"OPEN" 상태인 가게만 가져옴.
				        openRestaurants.add(rest);
				    }
				}
			}
		}
		for(Restaurant rest : openRestaurants) {
			SearchListDto dto = turnSearchList(rest);
			
			log.info("@@@@ 해시태그로 검색 list의 restaurant 아이디={}",dto.getRestId());
			
			list.add(dto);
		}
		
		for(SearchListDto dto : list) {
			if(!resultMap.containsKey(dto.getRestId())) {//레스토랑 중복 검사
				resultMap.put(dto.getRestId(), dto);
			}
		}
		
		List<SearchListDto> result = new ArrayList<>(resultMap.values());
		
		log.info("@@@@ 최종 리스트={}",result);
		return result;
	}
	//가게명으로 검색
	public List<SearchListDto> searchByRestaurantName(String keyword){
		//전체 dto 저장할 list
		List<SearchListDto> list = new ArrayList<SearchListDto>();
		
			
		//1. 레스토랑 이름에서 검색
		List<Restaurant> rests = restDao.searchAllByKeyword(keyword);
		
		for(Restaurant rest : rests) {//한 가게에 대하여
			
			SearchListDto dto = turnSearchList(rest);
			
			list.add(dto);
			
			log.info("@@@@ 가게명으로 검색 list의 restaurant 아이디={}",dto.getRestId());
			log.info("@@@@ 가게 명으로 검색 list={}",list);
		}
		
		return list;
	}
	
	//카테고리(방문목적,분위기,편의시설)와 키워드로 검색
	public List<SearchListDto> searchByCategoryAndKeyword(Long categoryId,String keyword){
		List<SearchListDto> list = new ArrayList<SearchListDto>();
		
		List<ReviewHashtag> list_HT = reviewHTDao.searchByCategoryAndKeyword(categoryId, keyword);
		for(ReviewHashtag ht : list_HT) {
			log.info(ht.getKeyword());
		}
		List<Restaurant> openRestaurants = new ArrayList<>();
		
		if(list_HT != null) {
			for(ReviewHashtag tag : list_HT) {
				log.info("@@@@ 해시태그 이름={}",tag.getKeyword());
				for(Review review : tag.getReviews()) {
					Restaurant rest = review.getRestaurant();//해시태그가 들어간 음식점을 가져옴
					log.info("@@@@@ openRest 전 rest 아이디={}",rest.getId());
					if (rest.getStatus() == RestaurantStatus.OPEN) {//"OPEN" 상태인 가게만 가져옴.
				        openRestaurants.add(rest);
				        
				    }
				}
			}
		}
		
		for(Restaurant rest : openRestaurants) {
			log.info("@@@ 카테고리로 검색 , 음식점 아이디 ={}",rest.getId());
			SearchListDto dto = turnSearchList(rest);

			list.add(dto);
		}
		return list;
	}
	
	
	//----- 한개의 레스토랑을 가져와서 SearchListDto로 변환 ---- 
	public SearchListDto turnSearchList(Restaurant rest) {
		
		//해시태그가 있으면 저장할 set
		Set<String> hashtags = new HashSet<String>();
		
		//음식점의 좋아요 수
		int totalMyPick = 0;
		//한 음식점에 대한 베스트 리뷰를 저장할 변수
		Review maxLikedReview = new Review();
		//베스트 댓글에 대한 좋아요 수
		int maxLikes = -1;
		//리뷰 개수
		int rvTotal=0;
		//음식점(리뷰 총) 평점
		double rvTotalRating;
		//음식점 메인 이미지
		String mainImg=null;
		
		int numLikes=0;//리뷰의 좋아요 수
				
		
		//가게 정보 가져오기
		totalMyPick = myPickDao.findByRestaurantId(rest.getId()).size();
		
		
		List<Review> rvResults = rvDao.findByRestaurantId(rest.getId());
		
		//리뷰 개수
		rvTotal = rvResults.size();
		if(rvTotal != 0) {
		
		log.info("@@@@ 리뷰 개수={}",rvTotal);
		//리뷰 평점 구하기
		double rvTotalRatingNum = reviewSvc.getTotalRating(rvResults);
		double result = Math.floor(rvTotalRatingNum * 10) / 10;
		rvTotalRating = result;
		
		for(Review review : rvResults) {
			
			
			
			List<ReviewLike> rvLikes= reviewLikeDao.findByReviewId(review.getId());
			if(rvLikes != null) {
				 numLikes =rvLikes.size();
			}
			if(numLikes > maxLikes) {
				 maxLikes = numLikes;
				 maxLikedReview = review;
			}
		}
		if(maxLikedReview.getReviewImages() == null || maxLikedReview.getReviewImages().isEmpty()){//만약 베스트 리뷰의 이미지가 없다면
			for(Review review : rvResults) {
				if(review.getReviewImages() != null && !review.getReviewImages().isEmpty()) {
					mainImg = review.getReviewImages().get(0).getImgUrl();
					break;//이미지가 있으면 종료.
				}
			}
		}else {//존재한다면 첫번째 이미지 사용
			mainImg = maxLikedReview.getReviewImages().get(0).getImgUrl();
		}
		if(maxLikedReview.getHashtags() == null || maxLikedReview.getHashtags().isEmpty()) {
			
		}else {
			for(ReviewHashtag tags : maxLikedReview.getHashtags()) {
				hashtags.add(tags.getKeyword());
			}
		}

		//Dto 생성
		SearchListDto dto =	SearchListDto.builder()
								.restId(rest.getId())
								.restName(rest.getName())
								.restCategory(rest.getCategory().getName())
								.restLat(rest.getLat())
								.restLon(rest.getLon())
								.myPickTotal(totalMyPick)
								.rvTotalRating(rvTotalRating)
								.rvTotal(rvTotal)
								.rvId(maxLikedReview.getId())
								.rvContent(maxLikedReview.getContent())
								.rvNickname(maxLikedReview.getMember().getNickname())
								.rvImg(mainImg)
								.rvLikes(maxLikes)
								.rvHashtags(hashtags)
								.build();
		log.info("@@@@dto={}",dto.getRvHashtags());
		
		return dto;
		}else {
			log.info("@@리뷰 없음");
			SearchListDto dto =	SearchListDto.builder()
					.restId(rest.getId())
					.restName(rest.getName())
					.restCategory(rest.getCategory().getName())
					.restLat(rest.getLat())
					.restLon(rest.getLon())
					.myPickTotal(totalMyPick)
					.build();
			return dto;
		}
	}
}
