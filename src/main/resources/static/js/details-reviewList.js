/**
 * details.html에 포함
 * details 페이지의 리뷰 리스트
 */

document.addEventListener('DOMContentLoaded', async()=>{
    //회원의 아이디
    let memberId ='';
    const restId = document.querySelector('input#restId').value;//음식점 아이디

    reviewListLoad(restId);
    loadHashtagsByCategory(restId);
    
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
                <div class="ht_container" style="display: flex; ">
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
                
                // 로그인한 회원이 리뷰 작성자인 경우 수정 버튼 추가
    if (memberId === review.memberId) { // memberId는 로그인한 회원의 ID, review.memberId는 리뷰 작성자의 ID
        const editButton = document.createElement('button');
        editButton.textContent = '리뷰 수정';
        editButton.className = 'btn btn-primary edit-review-btn';
        editButton.addEventListener('click', function() {
            // 리뷰 수정 페이지로 이동, URL에 리뷰 ID를 포함
            window.location.href = `/review/update/${review.id}`;
        });

        // 리뷰 요소에 수정 버튼 추가
        reviewElement.appendChild(editButton);
    }

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

    // 카테고리와 카테고리 이름을 매핑하는 객체
const categoryNames = {
  'visitPurpose': '방문 목적',
  'mood': '분위기',
  'convenience': '편의시설'
};

    // 순서대로 정렬하기 위한 배열
    const orderedCategories = ['visitPurpose', 'mood', 'convenience'];
    
    async function loadHashtagsByCategory(restaurantId) {
        try {
            const response = await axios.get(`/rest/details/hashtags/${restaurantId}`);
            const hashtagsByCategory = response.data;
            console.log(hashtagsByCategory);
    
            const htContainer = document.querySelector('.ht_container');
            htContainer.innerHTML = ''; 
    
            Object.keys(hashtagsByCategory).forEach((category, index) => {
                const hashtags = hashtagsByCategory[category];
                const categoryDiv = document.createElement('div');
                const categoryName = categoryNames[category] || category; // 카테고리 이름 매핑
    
                categoryDiv.classList.add('category-section');
                categoryDiv.innerHTML = `
                    <div class="category-title" style="margin-top: 10px;">
                        <span class="HT_${category}_All" style="font-size: 20px;">${categoryName}</span>
                    </div>
                    <div class="item2" id="hashtags-${index}">`;
    
                // 해시태그 추가
                hashtags.forEach((hashtag, hashtagIndex) => {
                    const span = document.createElement('span');
                    span.classList.add('ht_POV', 'badge', 'rounded-pill');
                    if (hashtagIndex >= 5) span.classList.add('hidden'); // 5개 초과하는 해시태그 숨김
                    span.textContent = hashtag;
                    categoryDiv.querySelector('.item2').appendChild(span);
                });
    
                // 해시태그가 5개 이상이면 '더보기'/'접기' 버튼 추가
                if (hashtags.length > 5) {
                    const buttonContainer = document.createElement('div');
                    buttonContainer.classList.add('more-button-container');
                
                    const moreButton = document.createElement('button');
                    moreButton.classList.add('btnMoreHt', 'btn');
                    moreButton.textContent = '더보기';
                    moreButton.onclick = function() {
                        // '더보기'/'접기' 상태 토글
                        this.classList.toggle('expanded');
                        // 버튼 텍스트 업데이트
                        this.textContent = this.classList.contains('expanded') ? '접기' : '더보기';
                        // 해시태그 표시 상태 토글
                        const hashtagsDiv = this.parentElement.previousElementSibling;
                        const hashtags = hashtagsDiv.querySelectorAll('.ht_POV');
                        hashtags.forEach((tag, index) => {
                            if (index >= 5) tag.classList.toggle('hidden');
                        });
                    };
                    buttonContainer.appendChild(moreButton);
                    categoryDiv.appendChild(buttonContainer);
                }
    
                htContainer.appendChild(categoryDiv);
            });
        } catch (error) {
            console.error('해시태그 정보를 불러오는 데 실패했습니다:', error);
        }
    }


    
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

