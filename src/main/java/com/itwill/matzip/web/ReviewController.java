package com.itwill.matzip.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.matzip.util.S3Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private final S3Utility s3Utility;
    
    @Autowired
    public ReviewController(S3Utility s3Utility) {
        this.s3Utility = s3Utility;
    }
	
    
	@GetMapping("/create")
    public void uploadPage() {
		log.info("GET - review create()");
	}
	
	@PostMapping("/register")
	public String registerReview(@RequestParam("file") MultipartFile file) {
	    log.info("POST - review register()");
	    
	    // 파일명 생성
	    String s3FileName = s3Utility.generateFileName();
	    
	    // 이미지 업로드
	    String imageUrl = s3Utility.uploadImageToS3(file, s3FileName);
	    log.info("이미지 URL: {}", imageUrl);
	    
	    return "/restaurant/detail"; // 리뷰 등록 후 상세페이지로 리다이렉트
	}


	
}
