package com.itwill.matzip.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewImage;
import com.itwill.matzip.dto.MemberMainHeaderRequestDto;
import com.itwill.matzip.dto.MyPickRequestDto;
import com.itwill.matzip.dto.MyReviewRequestDto;
import com.itwill.matzip.repository.MemberRepository;
import com.itwill.matzip.repository.MyPickRepository;
import com.itwill.matzip.repository.ReviewImageRepository;
import com.itwill.matzip.repository.ReviewRepository;
import com.itwill.matzip.util.S3Utility;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberInfoService {

	private final MemberRepository memDao;
	private final ReviewRepository reviewDao;
	private final MyPickRepository pickDao;
	private final ReviewImageRepository rlmgDao;
    private final S3Utility s3Util;
	
	public Member getMember(String username) {
		Member member = memDao.findByUsername(username).orElse(null);
		return member;
	}

	// 프로필 사진, 닉네임, 북마크, 리뷰 수 정보 가져옴
	public MemberMainHeaderRequestDto getProfileInfo(Member member) {
		log.info("getProfileInfo : member - {}", member);


		Long reviewCnt = reviewDao.countAllByMemberId(member.getId());
		Long myPickCnt = pickDao.countAllByMemberId(member.getId());

		return MemberMainHeaderRequestDto.builder().nickname(member.getNickname()).img(member.getImg())
				.reviewCnt(reviewCnt).pickCnt(myPickCnt).build();
	}

	// 내가 저장한 레스토랑 정보 가져오기
	public List<MyPickRequestDto> getMyPickRestaurant(Member member) {
		log.info("getMyPickRestaurant : member - {}", member);

		List<MyPickRequestDto> dto = null; // 북마크 된 레스토랑 저장

		List<MyPick> list = pickDao.findByMemberIdOrderById(member.getId());
		
		if (list != null) {
			dto = new ArrayList<>();

			for (MyPick p : list) {
				Long totalReview = 0L; // 총 리뷰수
				Double avgRatingReview = 0.0; // 리뷰 총 평균
				String img = null; // 리뷰 이미지 저장
				
				List<Review> reviews = reviewDao.findByRestaurantId(p.getRestaurant().getId());
				
				if (!reviews.isEmpty()) {
					totalReview = (long) reviews.size();

					// 총 별점 구하기
					avgRatingReview = getTotalRating(reviews);

					img = getImgUrl(p.getRestaurant().getId());
				}
				
				dto.add(MyPickRequestDto.builder().memberId(member.getId()).imgUrl(img)
						.restaurantId(p.getRestaurant().getId()).restaurantName(p.getRestaurant().getName())
						.categoryName(p.getRestaurant().getCategory().getName())
						.location(p.getRestaurant().getAddress()).totalSart(avgRatingReview).reviewAllCount(totalReview)
						.pickId(p.getId()).build());
			}
		}
		return dto;
	}

	//내가 쓴 리뷰 정보 가져오기
	public List<MyReviewRequestDto> getReviews(Member member) {
		log.info("getReviews : member - {}", member);
		List<MyReviewRequestDto> dto = null;
		
		List<Review> reviews = reviewDao.findByMemberIdOrderById(member.getId());
		
		if(!reviews.isEmpty()) {
			dto = new ArrayList<MyReviewRequestDto>();
			Double a = getTotalRating(reviews);
			
			for(Review review: reviews) {
				dto.add(MyReviewRequestDto.builder()
						.restaurantId(review.getRestaurant().getId())
						.restaurantName(review.getRestaurant().getName())
						.categoryName(review.getRestaurant().getCategory().getName())
						.location(review.getRestaurant().getAddress())
						.reviewId(review.getId())
						.createTime(review.getCreatedTime())
						.flavorScore((double)review.getFlavorScore())
						.serviceScore((double)review.getServiceScore())
						.priceScore((double)review.getPriceScore())
						.content(review.getContent())
						.mainImg(getImgUrl(review.getRestaurant().getId()))
						.reviewImg(getReviewImg(review.getId()))
						.totalAllReviewRating(a)
						.build());
			}
		}
		
		return dto;
	}
	
	//프로필 이미지 기본 이미지로 변경
	@Transactional
	public String changeProfileDefaultImg(String name) {
		String imgUrl =null;
		Member entity = memDao.findByUsername(name).orElse(null);
		
		
		if(entity != null) {
			entity.profileImgUpdate("https://domain-web-storage.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20240219_111445259.jpg");			
			imgUrl = "https://domain-web-storage.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20240219_111445259.jpg";
		}
		
		return imgUrl;
	}
	
	//프로필 이미지 커스텀 변경
	@Transactional
	public String changeProfileCtmImg(String name, MultipartFile imgFile) {
		String imgUrl =null;
		Member entity = memDao.findByUsername(name).orElse(null);
		
		if(!imgFile.isEmpty()) {
			try {
				imgUrl = s3Util.uploadImageToS3(imgFile, s3Util.generateFileName());	
				
				if(entity != null) {
					entity.profileImgUpdate(imgUrl);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return imgUrl;
	}
	
	//----------------------------------------------------------------------------------------------------------------
	//레스토랑에 해당하는 리뷰 이미지 첫번째 거 가져오기
	public String getImgUrl(Long restaurantId) {
		
		List<Review> reviews = reviewDao.findByRestaurantId(restaurantId);
		String rlmg = null;
		
		for(Review r : reviews) {
			List<ReviewImage> imgs = rlmgDao.findByReviewId(r.getId());
			
			if(!imgs.isEmpty()) {
				rlmg =imgs.get(0).getImgUrl().toString();
			}
		}
		return rlmg;
	}
	
	//내 리뷰에 해당하는 이미지들 가져오기
	public List<String> getReviewImg(Long reviewId){
		List<ReviewImage> imgs = rlmgDao.findByReviewId(reviewId);
		List<String> strImg = new ArrayList<String>();
		for(ReviewImage img : imgs) {
			strImg.add(img.getImgUrl());
		}
		
		return strImg;
	}
	
	//별점 평균 구하기
	public Double getTotalRating(List<Review> list) {
		int temp = 0;
		
		for (Review review : list) {
			temp += (review.getFlavorScore() + review.getPriceScore() + review.getServiceScore()) / 3;
		}

		return Math.floor(((double)temp / list.size())*10.0)/10.0;
	}
}
