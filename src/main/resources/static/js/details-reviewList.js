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
            console.log(reviews);

            let totalScoreSum = 0;
            let flavorScoreSum = 0;
            let priceScoreSum = 0;
            let serviceScoreSum = 0;
   
            const reviewListContainer = document.getElementById('reviewListContainer');
            reviewListContainer.innerHTML = ''; // 초기화

            reviews.hashtag
            
            reviews.forEach((review, reviewIndex) => {
                const reviewElement = document.createElement('div');
                reviewElement.className = 'review_post';

                // 평점 평균 계산
                const totalScore = (review.flavorScore + review.priceScore + review.serviceScore) / 3;
                const roundedTotalScore = Math.floor(totalScore * 10) / 10;

                totalScoreSum += totalScore;

                // 카테고리별 평점 합산(맛, 가격, 서비스)
                flavorScoreSum += review.flavorScore;
                priceScoreSum += review.priceScore;
                serviceScoreSum += review.serviceScore;

                // 리뷰 기본 정보
                let innerHTML = `
                <div class="ht_container">
                    <div class="profile_img" style="background-image: url('${review.memberImg}');"></div>
                    <div class="item2">
                        <p style="font-size: 23px; margin-left: 20px; font-weight: bold; margin-top: 15px; margin-bottom: 0;">${review.memberNickname}</p>
                        <div style="margin-left: 20px;">
                            <span class="total_score" style="font-size:17px; font-weight: bold; color:rgb(94, 94, 94);">${roundedTotalScore}점</span>
                            ${[1, 2, 3, 4, 5].map(index => {
                                if (index <= Math.floor(roundedTotalScore)) {
                                    return `<img class="star" data-index="${index}" src="/img/star_on.png" ${index === 1 ? 'style="margin-left: 0px;"' : ''}>`;
                                } else if (index === Math.ceil(roundedTotalScore) && roundedTotalScore % 1 >= 0.5) {
                                    return `<img class="star" data-index="${index}" src="/img/star_half.png" ${index === 1 ? 'style="margin-left: 0px;"' : ''}>`;
                                } else {
                                    return `<img class="star" data-index="${index}" src="/img/star_off.png" ${index === 1 ? 'style="margin-left: 0px;"' : ''}>`;
                                }
                            }).join('')}
                            <span style="margin-left: 5px; font-size: 18px; color:rgb(94, 94, 94);">${review.formattedRegisterDate}</span>
                        </div>
                        <div class="review_scores" style="margin-left: 10px;">
                            <span class="detail_rating">맛 <img src="/img/miniStar.png" class="miniStar">${review.flavorScore}</span>
                            <span class="detail_rating">가격 <img src="/img/miniStar.png" class="miniStar">${review.priceScore}</span>
                            <span class="detail_rating">서비스 <img src="/img/miniStar.png" class="miniStar">${review.serviceScore}</span>
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
                            <span class="visually-hidden">이전</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators${reviewIndex}" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">다음</span>
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

            // 리뷰 총 개수 업데이트
            const totalReviewCountElement1 = document.querySelector('.title.total-review');
            const totalReviewCountElement2 = document.querySelector('.evalCnt');
            totalReviewCountElement1.textContent = `${reviews.length}건의 방문자 평가`;
            totalReviewCountElement2.textContent = `(${reviews.length}명의 평가)`;

            // 평점 평균 계산 및 별점 표시
            const averageScore = reviews.length > 0 ? totalScoreSum / reviews.length : 0;
            const roundedAverageScore = Math.floor(averageScore * 10) / 10;

            const evalAvg1Element1 = document.querySelector('.evalAvg1');
            const evalAvg1Element2 = document.querySelector('.evalAvg2');
            evalAvg1Element1.textContent = `${roundedAverageScore}점`;
            evalAvg1Element2.textContent = `${roundedAverageScore}점`;

            displayAverageRatingStars(roundedAverageScore); // 별점 표시 함수

            // 카테고리별 평점 평균
            const flavorScoreAvg = flavorScoreSum / reviews.length;
            const priceScoreAvg = priceScoreSum / reviews.length;
            const serviceScoreAvg = serviceScoreSum / reviews.length;

            const ratingCategoryAvgContainer = document.querySelector('.rating_category_avg');
            ratingCategoryAvgContainer.innerHTML = `
            <div class="review_scores_all" >
                <span class="flavor_rating_avg">맛 <img src="/img/miniStar.png" class="miniStar">${flavorScoreAvg.toFixed(1)}</span> 
                <span class="price_rating_avg">가격 <img src="/img/miniStar.png" class="miniStar">${priceScoreAvg.toFixed(1)}</span>
                <span class="service_rating_avg">서비스 <img src="/img/miniStar.png" class="miniStar">${serviceScoreAvg.toFixed(1)}</span>
            </div>`;


            // 리뷰 이미지 클릭 이벤트 리스너 추가
            addReviewImageClickListener();
        } catch (error) {
            console.error('리뷰 목록을 불러오는 데 실패했습니다:', error);
        }
    }// -- reviewListLoad END --
    

    
    // 리뷰 이미지 클릭 이벤트 리스너 함수
    function addReviewImageClickListener() {
        document.querySelectorAll('.carousel-img').forEach(image => {
            image.addEventListener('click', function () {
                showImageInModal(image.src);
            });
        });
    }

    // 이미지 모달 함수
    function showImageInModal(imageSrc) {
        const modalImageContainer = document.querySelector('.imgContainer_img');
        modalImageContainer.innerHTML = `<img src="${imageSrc}" style="max-width:100%;"/>`;
        $('#gallryModal').modal('show'); // 모달 표시
    }

    // 별점 표시 함수
function displayAverageRatingStars(roundedAverageScore) {
    // 첫 번째 별점 컨테이너 선택
    const ratingStarContainer = document.querySelector('.rating_star');
    ratingStarContainer.innerHTML = ''; // 이전 별점 초기화

    // 두 번째 별점 컨테이너 선택
    const ratingStarContainer2 = document.querySelector('.rating_star2');
    ratingStarContainer2.innerHTML = ''; // 이전 별점 초기화

    let starsHTML = '';

    for (let index = 1; index <= 5; index++) {
        if (index <= Math.floor(roundedAverageScore)) {
            starsHTML += `<img class="bigStar" data-index="${index}" src="/img/star_on.png">`;
        } else if (index === Math.ceil(roundedAverageScore) && roundedAverageScore % 1 >= 0.5) {
            starsHTML += `<img class="bigStar" data-index="${index}" src="/img/star_half.png">`;
        } else {
            starsHTML += `<img class="bigStar" data-index="${index}" src="/img/star_off.png">`;
        }
    }

    // 두 컨테이너에 동일한 별점 HTML 삽입
    ratingStarContainer.innerHTML = starsHTML;
    ratingStarContainer2.innerHTML = starsHTML;
}

    
});//end document

