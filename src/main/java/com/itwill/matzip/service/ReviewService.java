package com.itwill.matzip.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.matzip.domain.Review;
import com.itwill.matzip.domain.ReviewHashtag;
import com.itwill.matzip.domain.ReviewImage;
import com.itwill.matzip.repository.ReviewHashtagRepository;
import com.itwill.matzip.repository.ReviewImageRepository;
import com.itwill.matzip.repository.ReviewRepository;
import com.itwill.matzip.util.S3Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewService {
	
	private final ReviewRepository reviewDao;
	private final ReviewHashtagRepository reviewHTDao;
	private final ReviewImageRepository reviewImageDao;
	private final S3Utility s3Util;
	
	@Autowired
	public ReviewService(ReviewRepository reviewDao, ReviewImageRepository reviewImageDao, 
						ReviewHashtagRepository reviewHTDao, S3Utility s3Util) {
		this.reviewDao = reviewDao;
		this.reviewHTDao = reviewHTDao;
		this.reviewImageDao = reviewImageDao;
		this.s3Util = s3Util;
	}
	
	
	
	@Transactional
    public void saveReview(Review review, List<MultipartFile> images, List<String> hashtags) {
        // 리뷰저장
        Review savedReview = reviewDao.save(review);

        // 이미지 업로드, ReviewImage 엔티티 저장
        images.forEach(image -> {
            String imageUrl = s3Util.uploadImageToS3(image, s3Util.generateFileName());
            ReviewImage reviewImage = new ReviewImage(null, savedReview, imageUrl);
            reviewImageDao.save(reviewImage);
        });

        // 해시태그 저장
        hashtags.forEach(hashtag -> {
            ReviewHashtag reviewHashtag = new ReviewHashtag(); // 해시태그 생성/찾기
            reviewHTDao.save(reviewHashtag);
            savedReview.getHashtags().add(reviewHashtag);
        });

        // 해시태그와 연결된 리뷰 업데이트
        reviewDao.save(savedReview);
    }
	
	

}
