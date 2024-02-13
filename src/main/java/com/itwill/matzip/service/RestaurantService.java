package com.itwill.matzip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.repository.BusinessHourRepository;
import com.itwill.matzip.repository.MenuRepository;
import com.itwill.matzip.repository.RestaurantRepository;

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
	
	//음식점 전체 목록 가져오기
	public List<Restaurant> findAllMaps(){
		log.info("@@@ findAllMaps");
		
		return restDao.findAll();
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
	
	
}
