package com.itwill.matzip.MemberRepo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.matzip.service.MemberInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ReviewTest {
	
	@Autowired MemberInfoService miSvc;
	
	//레스토랑 아이디를 리뷰 이미지 가져오는 테스트 하기
	//@Test
	public void getReviewMainImg() {
		String main = miSvc.getImgUrl(1l);
		log.info(main);
	}
	
	//리뷰 아이디를 가지고 이미지 전체 가져오기
	@Test
	public void getReviewMyImg() {
		List<String> img = miSvc.getReviewImg(13l);
		
		img.forEach((x) -> log.info(x.toString()));
	}

}
