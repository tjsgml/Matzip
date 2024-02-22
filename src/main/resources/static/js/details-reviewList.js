/**
 * details.html에 포함
 * details 페이지의 리뷰 리스트
 */

document.addEventListener('DOMContentLoaded', async()=>{
	//회원의 아이디
	let memberId ='';
	const restId = document.querySelector('input#restId').value;//음식점 아이디

    reviewListLoad(restId);
	

    // 리뷰 목록 ----------------------------------------------------------------------
    async function reviewListLoad(restaurantId) {
        try {
            const response = await axios.get(`/rest/details/reviews/${restaurantId}`);
            const reviews = response.data;
            
            const reviewListContainer = document.getElementById('reviewListContainer');
            reviewListContainer.innerHTML = ''; // 초기화
            
            reviews.forEach((review, reviewIndex) => {
                const reviewElement = document.createElement('div');
                reviewElement.className = 'review_post';
                console.log(reviews);

                // 평점 평균 계산
                const totalScore = (review.flavorScore + review.priceScore + review.serviceScore) / 3;
                const roundedTotalScore = Math.floor(totalScore * 10) / 10;
                
                // 리뷰 기본 정보
                let innerHTML = `
                <div class="ht_container">
                    <div class="profile_img" style="background-image: url('${review.memberImg}');"></div>
                    <div class="item2">
                        <p style="font-size: 23px; margin-left: 20px; font-weight: bold; margin-top: 15px; margin-bottom: 0;">${review.memberNickname}</p>
                        ${[1, 2, 3, 4, 5].map(index => {
                            if (index <= Math.floor(roundedTotalScore)) {
                                // 총 평점의 정수 부분에 해당하는 별은 모두 색칠
                                return `<img class="star" data-index="${index}" src="/img/star_on.png" ${index === 1 ? 'style="margin-left: 20px;"' : ''}>`;
                            } else if (index === Math.ceil(roundedTotalScore) && roundedTotalScore % 1 >= 0.5) {
                                // 소수점이 0.5 이상인 경우에만 별 반개
                                return `<img class="star" data-index="${index}" src="/img/star_half.png" ${index === 1 ? 'style="margin-left: 20px;"' : ''}>`;
                            } else {
                                // 그 외
                                return `<img class="star" data-index="${index}" src="/img/star_off.png" ${index === 1 ? 'style="margin-left: 20px;"' : ''}>`;
                            }
                        }).join('')}
                        
                        <span style="font-size: 20px; color:rgb(94, 94, 94);">${review.formattedRegisterDate}</span>
                        
                        <div class="review_scores" style="margin-left: 10px;">
                            <span class="detail_rating">맛 <img src="/img/miniStar.png" class="miniStar">${review.flavorScore}</span>
                            <span class="detail_rating">가격 <img src="/img/miniStar.png" class="miniStar">${review.priceScore}</span>
                            <span class="detail_rating">서비스 <img src="/img/miniStar.png" class="miniStar">${review.serviceScore}</span>
                            <span class="total_score" style="font-size: 20px; color:rgb(94, 94, 94);">총 평점: ${roundedTotalScore}</span>
                        </div>
                    </div>
                </div>
                <p class="review_contents btxt" style="font-size: 18px; margin-top:10px;">${review.content}</p>`;

                // 리뷰 이미지 캐러셀
                if (review.reviewImages.length > 0) {
                innerHTML += `
                <div id="carouselExampleIndicators${reviewIndex}" class="carousel slide carousel-dark" >
                    <div class="carousel-indicators">${review.reviewImages.map((_, index) => `<button type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide-to="${index}" ${index === 0 ? 'class="active"' : ''} aria-label="Slide ${index + 1}"></button>`).join('')}</div>
                    <div class="carousel-inner">${review.reviewImages.map((imgUrl, index) => `<div class="carousel-item ${index === 0 ? 'active' : ''}"><img src="${imgUrl}" class="d-block w-100 carousel-img" alt="Review image ${index}"></div>`).join('')}</div>
                    ${review.reviewImages.length > 1 ? `
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>` : ''}
                </div>`;
            }

                // 해시태그 및 공감 버튼
                innerHTML += `
                <div class="review_ht_list">
                    ${review.hashtags.map(hashtag => `<span class="ht_POV badge rounded-pill">${hashtag}</span>`).join(' ')}
                </div>
                <button class="btn btn-outline-danger">공감</button>`;
                
                reviewElement.innerHTML = innerHTML;
                reviewListContainer.appendChild(reviewElement);
            });

            // 리뷰 이미지 클릭 이벤트 리스너 추가
            addReviewImageClickListener();
        } catch (error) {
            console.error('리뷰 목록을 불러오는 데 실패했습니다:', error);
        }
    }
	//--------------------------------------------------------------------------------------------------------


	/**
 * details.html에 포함
 * details 페이지의 리뷰 리스트
 */

document.addEventListener('DOMContentLoaded', async()=>{
    //회원의 아이디
    let memberId ='';
    const restId = document.querySelector('input#restId').value;//음식점 아이디

    reviewListLoad(restId);
    

    // 리뷰 목록 ----------------------------------------------------------------------
    async function reviewListLoad(restaurantId) {
        try {
            const response = await axios.get(`/rest/details/reviews/${restaurantId}`);
            const reviews = response.data;
            
            const reviewListContainer = document.getElementById('reviewListContainer');
            reviewListContainer.innerHTML = ''; // 초기화
            
            reviews.forEach((review, reviewIndex) => {
                const reviewElement = document.createElement('div');
                reviewElement.className = 'review_post';

                // 평점 평균 계산
                const totalScore = (review.flavorScore + review.priceScore + review.serviceScore) / 3;
                const roundedTotalScore = Math.floor(totalScore * 10) / 10;
                
                // 리뷰 기본 정보
                let innerHTML = `
                <div class="ht_container">
                    <div class="profile_img" style="background-image: url('${review.memberImg}');"></div>
                    <div class="item2">
                        <p style="font-size: 23px; margin-left: 20px; font-weight: bold; margin-top: 15px; margin-bottom: 0;">${review.memberNickname}</p>
                        ${[1, 2, 3, 4, 5].map((index, arrIndex) => `<img class="star" data-index="${index}" src="/img/star_${index <= review.flavorScore ? 'on' : 'off'}.png"${arrIndex === 0 ? ' style="margin-left: 20px;"' : ''}>`).join('')}
                        <span style="font-size: 20px; color:rgb(94, 94, 94);">${review.formattedRegisterDate}</span>
                        <div class="review_scores" style="margin-left: 10px;">
                            <span class="detail_rating">맛 <img src="/img/miniStar.png" class="miniStar">${review.flavorScore}</span>
                            <span class="detail_rating">가격 <img src="/img/miniStar.png" class="miniStar">${review.priceScore}</span>
                            <span class="detail_rating">서비스 <img src="/img/miniStar.png" class="miniStar">${review.serviceScore}</span>
                            <span class="total_score" style="font-size: 20px; color:rgb(94, 94, 94);">총 평점: ${roundedTotalScore}</span>
                        </div>
                    </div>
                </div>
                <p class="review_contents btxt" style="font-size: 18px; margin-top:10px;">${review.content}</p>`;

                // 리뷰 이미지 캐러셀
                if (review.reviewImages.length > 0) {
                    innerHTML += `
                    <div id="carouselExampleIndicators${reviewIndex}" class="carousel slide carousel-dark ${review.reviewImages.length === 1 ? ' d-none' : ''}" >
                        <div class="carousel-indicators${review.reviewImages.length === 1 ? ' d-none' : ''}">`;
                    review.reviewImages.forEach((_, index) => {
                        innerHTML += `<button type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide-to="${index}" ${index === 0 ? 'class="active"' : ''} aria-label="Slide ${index + 1}"></button>`;
                    });
                    innerHTML += `</div><div class="carousel-inner">`;
                    review.reviewImages.forEach((imgUrl, index) => {
                        innerHTML += `<div class="carousel-item ${index === 0 ? 'active' : ''}"><img src="${imgUrl}" class="d-block w-100 carousel-img" alt="Review image"></div>`;
                    });
                    innerHTML += `</div>`;
                    if (review.reviewImages.length > 1) {
                        innerHTML += `
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>`;
                    }
                    innerHTML += `</div>`;
                }

                // 해시태그 및 공감 버튼
                innerHTML += `
                <div class="review_ht_list">
                    ${review.hashtags.map(hashtag => `<span class="ht_POV badge rounded-pill">${hashtag}</span>`).join(' ')}
                </div>
                <button class="btn btn-outline-danger">공감</button>`;
                
                reviewElement.innerHTML = innerHTML;
                reviewListContainer.appendChild(reviewElement);
            });

            // 리뷰 이미지 클릭 이벤트 리스너 추가
            addReviewImageClickListener();
        } catch (error) {
            console.error('리뷰 목록을 불러오는 데 실패했습니다:', error);
        }
    }
    //--------------------------------------------------------------------------------------------------------


    
    
    
    //----------------함수들------------------------

        
    
    // 리뷰 이미지 클릭 이벤트 리스너 함수
      function addReviewImageClickListener() {
          document.querySelectorAll('.carousel-img').forEach(image => {
              image.addEventListener('click', function () {
                  showImageInModal(image.src);
              });
          });
      }

      // 이미지를 모달에 표시하는 함수
      function showImageInModal(imageSrc) {
          const modalImageContainer = document.querySelector('.imgContainer_img');
          modalImageContainer.innerHTML = `<img src="${imageSrc}" style="max-width:100%; "/>`; // 이미지를 100% 너비로 설정
          $('#gallryModal').modal('show'); // 모달 표시
      }


 });//end document
	
	
	//----------------함수들------------------------

		
	
    // 리뷰 이미지 클릭 이벤트 리스너 함수
      function addReviewImageClickListener() {
          document.querySelectorAll('.carousel-img').forEach(image => {
              image.addEventListener('click', function () {
                  showImageInModal(image.src);
              });
          });
      }

      // 이미지를 모달에 표시하는 함수
      function showImageInModal(imageSrc) {
          const modalImageContainer = document.querySelector('.imgContainer_img');
          modalImageContainer.innerHTML = `<img src="${imageSrc}" style="max-width:100%; "/>`; // 이미지를 100% 너비로 설정
          $('#gallryModal').modal('show'); // 모달 표시
      }


 });//end document