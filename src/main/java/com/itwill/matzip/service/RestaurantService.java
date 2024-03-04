package com.itwill.matzip.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.itwill.matzip.repository.*;
import com.itwill.matzip.repository.UpdateRequest.UpdateRequestRepository;
import com.itwill.matzip.repository.member.MemberRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import com.itwill.matzip.repository.reviewHashtag.ReviewHashtagRepository;
import com.itwill.matzip.util.DateTimeUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.domain.ReviewImage;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.UpdateRequest;
import com.itwill.matzip.domain.enums.Expose;
import com.itwill.matzip.dto.MyPickRequestDto;
import com.itwill.matzip.dto.ReviewListDto;
import com.itwill.matzip.dto.TagRestaurantRequestDto;
import com.itwill.matzip.dto.UpdateRequestItemDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    //RESTAURANT 테이블
    private final RestaurantRepository restDao;
    //BUSINESS_HOUR 테이블
    private final BusinessHourRepository bsDao;
    //MENU 테이블
    private final MenuRepository menuDao;
    //MYPICK 테이블
    private final MyPickRepository myPickDao;
    //MEMBER 테이블
    private final MemberRepository memberDao;
    //UPDATE_REQUEST 테이블
    private final UpdateRequestRepository URdao;
    //리뷰 서비스 - 리뷰에 있는 총점 메서드 사용하기 위함
    private final ReviewService reviewSvc;
    
	private final ReviewHashtagRepository reviewTagDao;


    //음식점 전체 목록 가져오기
    public List<Restaurant> findAllMaps() {
        log.info("@@@ findAllMaps");
        List<Restaurant> list = restDao.findAllByStatus(RestaurantStatus.OPEN);
        log.info(list.toString());
        return list;
    }

    //음식점 한개 목록 가져오기
    public Restaurant findOneRest(Long id) {
        log.info("@@@ findOneRest() 호출");
        return restDao.findById(id).orElseThrow();
    }

    //영업 시간 가져오기
    public List<BusinessHour> findBsHour(Long id) {
        log.info("@@@ findBsHour() 호출");

        Restaurant rest = restDao.findById(id).orElseThrow();

        List<BusinessHour> bsHourList = bsDao.findByRestaurant(rest);

        return bsHourList;

    }

    //메뉴들 가져오기
    public List<Menu> findMenus(Long id) {
        Restaurant rest = restDao.findById(id).orElseThrow();

        List<Menu> menuList = menuDao.findByRestaurant(rest);

        return menuList;
    }

    //멤버 id 가져오기
    public Long findMemberId(String username) {
        Member member = memberDao.findByUsername(username).orElseThrow();

        return member.getId();
    }

    //좋아요 정보 가져오기- 있으면 1, 없으면 null을 반환.
    public Long checkMyPick(Long memberId, Long restId) {

        MyPick myPick = myPickDao.findByMemberIdAndRestaurantId(memberId, restId);

        if (myPick == null) {
            return null;
        } else {
            return myPick.getId();
        }
    }

    //좋아요 삭제
    public void deleteMyPick(Long myPickId) {
        myPickDao.deleteById(myPickId);

    }

    //좋아요 생성
    public Long registerMyPick(Long memberId, Long restId) {

        Member member = memberDao.findById(memberId).orElse(null);
        Restaurant rest = restDao.findById(restId).orElse(null);

        MyPick myPick = MyPick.builder()
                .member(member)
                .restaurant(rest)
                .build();

        myPick = myPickDao.save(myPick);

        return myPick.getId();
    }

    // 수정 요청 사항 삽입
    public void updateRequest(UpdateRequestItemDto dto) {
        Restaurant rest = restDao.findById(dto.getRestId()).orElse(null);
        UpdateRequest ur = UpdateRequest.builder()
                .restaurant(rest)
                .content(dto.getContent())
                .build();
        URdao.save(ur);
    }

    /* 은겸 추가 */
    private final ReviewRepository reviewDao;

    public List<ReviewListDto> getReviewsForRestaurant(Long restaurantId) {
        List<Review> reviews = reviewDao.findByRestaurantId(restaurantId);
        return reviews.stream().map(review -> ReviewListDto.builder()
                        .id(review.getId())
                        .content(review.getContent())
                        .flavorScore(review.getFlavorScore())
                        .serviceScore(review.getServiceScore())
                        .priceScore(review.getPriceScore())
                        .formattedRegisterDate(DateTimeUtil.formatLocalDateTime(review.getCreatedTime())) // 포맷팅된 날짜
                        .memberNickname(review.getMember().getNickname())
                        .memberImg(review.getMember().getImg())
                        .reviewImages(review.getReviewImages().stream()
                                .map(ReviewImage::getImgUrl)
                                .collect(Collectors.toList()))
                        .hashtags(review.getHashtags().stream()
                                .map(ReviewHashtag::getKeyword)
                                .collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }


    // 내가 저장한 레스토랑 정보 가져오기
    public Page<MyPickRequestDto> getMyPickRestaurant(Long id, int p) {
        log.info("getMyPickRestaurant : member - {}, p - {}", id, p);

        List<MyPickRequestDto> dto = null; // 북마크 된 레스토랑 저장

        List<MyPick> pList = myPickDao.findByMemberIdOrderById(id);

        if (pList != null) {
            dto = new ArrayList<>();

            for (MyPick pick : pList) {
                Long totalReview = 0L; // 총 리뷰수
                Double avgRatingReview = 0.0; // 리뷰 총 평균
                String img = null; // 리뷰 이미지 저장

                List<Review> reviews = reviewDao.findByRestaurantId(pick.getRestaurant().getId());

                //식당 평점 구하기
                if (!reviews.isEmpty()) {
                    totalReview = (long) reviews.size();

                    // 총 별점 구하기
                    avgRatingReview = reviewSvc.getTotalRating(reviews);
                    img = reviewSvc.getImgUrl(pick.getRestaurant().getId());
                }

                dto.add(MyPickRequestDto.builder().memberId(id).imgUrl(img)
                        .restaurantId(pick.getRestaurant().getId()).restaurantName(pick.getRestaurant().getName())
                        .categoryName(pick.getRestaurant().getCategory().getName())
                        .location(pick.getRestaurant().getAddress()).totalSart(avgRatingReview).reviewAllCount(totalReview)
                        .pickId(pick.getId()).build());
            }
        }

        PageRequest pageRequest = PageRequest.of(p, 1);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), dto.size());

        Page<MyPickRequestDto> list = new PageImpl<>(dto.subList(start, end), pageRequest, dto.size());

        return list;
    }

	public List<TagRestaurantRequestDto> getRestaurantByHashTag() {
		log.info("getRestaurantByHashTag");
		Double restTotalRating = 0.0;
		List<TagRestaurantRequestDto> dto = new ArrayList<>();

		//노출 표시한 태그들 찾기
		List<ReviewHashtag> list = reviewTagDao.findByExposeOrderById(Expose.Y);

		for (ReviewHashtag exposeY : list) {
			//태그에 해당된 리뷰 가져오기
			Set<Review> reviewList = exposeY.getReviews();
			Set<Restaurant> restList = new HashSet<>();
			List<MyPickRequestDto> pickList = new ArrayList<>();
			
			//리뷰에 해당하는 레스토랑 가져오기
			for (Review r : reviewList) {
				restList.add(r.getRestaurant());				
			}
			
			//가져온 레스토랑들의 리뷰들을 가져와 총점 계산
			for (Restaurant re : restList) {
				List<Review> l = reviewDao.findByRestaurantId(re.getId());
				restTotalRating = reviewSvc.getTotalRating(l);
				
				pickList.add(MyPickRequestDto.builder()
						.imgUrl(reviewSvc.getImgUrl(re.getId()))
						.restaurantId(re.getId())
						.restaurantName(re.getName())
						.categoryName(re.getCategory().getName())
						.totalSart(restTotalRating)
						.build());
				
				restTotalRating=0.0;
			}

			//dto에 저장하기
			dto.add(TagRestaurantRequestDto.builder().tagId(exposeY.getId()).tagKeyword(exposeY.getKeyword())
					.rest(pickList).restLength(restList.size()).build());
			restTotalRating = 0.0;
		}
		return dto;
	}
}
