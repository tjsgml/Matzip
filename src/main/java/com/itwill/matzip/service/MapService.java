package com.itwill.matzip.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewImage;
import com.itwill.matzip.dto.MapReviewDto;
import com.itwill.matzip.dto.MostLikedReviewDto;
import com.itwill.matzip.repository.CategoryRepository;
import com.itwill.matzip.repository.MyPickRepository;
import com.itwill.matzip.repository.review.ReviewRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MapService {
	//리뷰 테이블
	private final ReviewRepository rvDao;
	
	//마이픽 테이블
	private final MyPickRepository mpDao;
	
	//카테고리 테이블
	private final CategoryRepository ctDao;
	
	//레스토랑 테이블
	private final RestaurantRepository restDao;
	

	
	 @Autowired
	    private EntityManager entityManager;
	//평점수를 위한 리뷰 가져오기.
	public List<MapReviewDto> getReviews(Long id){
		   List<Review> reviews = rvDao.findByRestaurantId(id);
	        List<MapReviewDto> dtos = new ArrayList<>();
	        
	        for (Review review : reviews) {
	            dtos.add(toMapReviewDto(review)); // 각 리뷰 엔터티를 DTO로 변환하여 리스트에 추가
	        }
	        
	        
	        return dtos;
			
	}
	//공감수가 가장 많은 리뷰 가져오기.
	  public List<MostLikedReviewDto> findMostLikedReviewByRestaurantId(Long restaurantId) {
	        String sqlQuery = "SELECT r.content, m.nickname, COUNT(rl.review_fk) " +
	                          "FROM review r " +
	                          "LEFT JOIN member m ON r.member_fk = m.id " +
	                          "LEFT JOIN review_like rl ON r.review_pk = rl.review_fk " +
	                          "WHERE r.restaurant_fk = :restaurantId " +
	                          "GROUP BY r.content, m.nickname, r.restaurant_fk";
	        
	        Query query = entityManager.createNativeQuery(sqlQuery);
	        query.setParameter("restaurantId", restaurantId);
	        
	        List<Object[]> resultList = query.getResultList();
	        
	        List<MostLikedReviewDto> dtoList = new ArrayList<>();
	        for (Object[] row : resultList) {
	            MostLikedReviewDto dto = new MostLikedReviewDto();
	            dto.setContent((String) row[0]);
	            dto.setNickname((String) row[1]);
	            dto.setTotalLikes(((Number) row[2]).longValue());
	            dtoList.add(dto);
	        }
	        
	        log.info("평점 리뷰={}",dtoList.toString());
	        return dtoList;
	    }
	
	 // 리뷰 엔터티를 DTO로 변환하는 메서드
    private MapReviewDto toMapReviewDto(Review review) {
        MapReviewDto dto = new MapReviewDto();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setFlavorScore(review.getFlavorScore());
        dto.setServiceScore(review.getServiceScore());
        dto.setPriceScore(review.getPriceScore());
        
        // 리뷰 이미지 경로를 가져와서 설정
        List<String> imagePaths = review.getReviewImages().stream()
                                 .map(ReviewImage::getImagePath)
                                 .collect(Collectors.toList());
        dto.setReviewImages(imagePaths);
        
        return dto;
    }
    
    //음식점 좋아요 수 가져오기
    public Integer getTotalMyPicks(Long restaurantId) {
    	
    	List<MyPick> myPicks = mpDao.findByRestaurantId(restaurantId);
    	
    	return myPicks.size();
    }
    
    //카테고리 목록 가져오기.
    public List<Category> getAllCategory(){
    	List<Category> list = ctDao.findByOrderByListOrderAsc();
    	
    	return list;
    }
    //카테고리에 따른 음식점 리스트 가져오기.
    
    public List<Restaurant> getRestaurantsByCategory(Integer categoryId){
    	return restDao.findByStatusAndCategoryId(RestaurantStatus.OPEN,categoryId);
    }
    
    //전체 검색 결과
    public List<Restaurant> searchAllByKeyword(String keyword){
    	return restDao.searchAllByKeyword(keyword);
    }
    
    //카테고리별 검색 결과
    public List<Restaurant> searchByCategoryAndKeyword(Integer categoryId, String keyword){
    	return restDao.searchByCategoryAndKeyword(categoryId,keyword);
    }
}
