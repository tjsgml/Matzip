package com.itwill.matzip.service;

import java.util.List;

import com.itwill.matzip.repository.*;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Member;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.MyPick;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.UpdateRequest;
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
	
	
	//음식점 전체 목록 가져오기
	public List<Restaurant> findAllMaps(){
		log.info("@@@ findAllMaps");
		List<Restaurant> list =restDao.findAll();
		log.info(list.toString());
		return list;
	}
	
	//음식점 한개 목록 가져오기
	public Restaurant findOneRest(Long id) {
		log.info("@@@ findOneRest() 호출");
		return restDao.findById(id).orElseThrow();
	}
	
	//영업 시간 가져오기
	public List<BusinessHour> findBsHour(Long id){
		log.info("@@@ findBsHour() 호출");
		
		Restaurant rest = restDao.findById(id).orElseThrow();
		
		List<BusinessHour> bsHourList = bsDao.findByRestaurant(rest);
		
		return bsHourList;
		
	}
	//메뉴들 가져오기
	public List<Menu> findMenus(Long id){
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
	public Long checkMyPick(Long memberId,Long restId) {
		
		MyPick myPick = myPickDao.findByMemberIdAndRestaurantId(memberId, restId);
		
		if(myPick == null) {
			return null;
		}else {
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
		UpdateRequest ur = UpdateRequest.builder()
										.restId(dto.getRestId())
										.content(dto.getContent())
										.build();
		URdao.save(ur);
	}
	
	/* 은겸 추가 */
	private final ReviewRepository reviewDao;
	
	public List<Review> findReviewsByRestaurantId(Long restaurantId) {
	    return reviewDao.findByRestaurantId(restaurantId);
	}

	
	
}
