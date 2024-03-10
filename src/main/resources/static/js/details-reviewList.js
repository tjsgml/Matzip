/**
 * details.html에 포함
 * details 페이지의 리뷰 리스트
 */

document.addEventListener('DOMContentLoaded', async () => {

    // 현재 로그인한 사용자의 닉네임
    const loggedInUserNickname = document.getElementById('loggedInUserNickname').textContent.trim();
    
    let currentPage = 0;
    let isFetching = false; // 현재 데이터를 불러오는 중인지 확인하는 플래그
    
    const restId = document.querySelector('input#restId').value;//음식점 아이디

    reviewListLoad(restId);
    loadHashtagsByCategory(restId);
    
     // 스크롤
    function onScroll() {
        console.log("스크롤@@@@@@@");
         console.log(`innerHeight + scrollY: ${window.innerHeight + window.scrollY}, body.offsetHeight: ${document.body.offsetHeight}`);
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 10 && !isFetching) { // 끝에 거의 도달했을 때 로딩
            console.log("스크롤@@@@@@@ 되냐");
            loadMoreData();
        }
    }
    
    async function loadMoreData() {
        isFetching = true;
        currentPage++;
        await reviewListLoad(restId, currentPage);
        isFetching = false;
    }


    

    // 리뷰 목록 ----------------------------------------------------------------------
    async function reviewListLoad(restaurantId, currentPage) {
        try {
            const response = await axios.get(`/rest/details/reviews/${restaurantId}?page=${currentPage}&size=10`);
            //const { content, last } = response.data; // last: 마지막 페이지 여부
            const reviews = response.data;
            
            let totalScoreSum = 0;
            let flavorScoreSum = 0;
            let priceScoreSum = 0;
            let serviceScoreSum = 0;

            const reviewListContainer = document.getElementById('reviewListContainer');
            //reviewListContainer.innerHTML = ''; // 초기화
            
            console.log(response.data);

            if (reviews.length === 0 && currentPage === 0) {
                // 리뷰가 없을 때 
                const noDataHTML = `
                    <div class="card">
                        <div id="none-data-div" class="card-body pt-5">
                            <div class="row fw-semibold text-center" style="font-size: 20px;">
                                <div class="col my-5">
                                    <p style="font-size: 50px">🥄🥺🥢</p>
                                    <div>아직 등록된 리뷰가 없어요..</div>
                                </div>
                            </div>
                        </div>
                    </div>`;
                reviewListContainer.innerHTML = noDataHTML;
                return; 
            }           
            

            reviews.forEach((review, reviewIndex) => {
                console.log(`Review ${reviewIndex}:`, review.memberNickname, loggedInUserNickname, review.memberNickname === loggedInUserNickname);

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
                            <span class="total_score" style="font-size:17px; font-weight: bold; color:rgb(94, 94, 94);">${roundedTotalScore}</span>
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
                <p class="review_contents btxt">${review.content}</p>`;

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
                <div class="review-btn-container">
                    
                </div>`;

                reviewElement.innerHTML = innerHTML;

                const btnContainer = reviewElement.querySelector('.review-btn-container');

                // 로그인한 회원이 리뷰 작성자인 경우 수정/삭제 버튼 추가
                if (loggedInUserNickname && review.memberNickname === loggedInUserNickname) {
                    // 수정 버튼 생성
                    const editButton = document.createElement('button');
                    editButton.textContent = 'ㅤ수정ㅤ';
                    editButton.className = 'btn edit-review-btn';
                    editButton.addEventListener('click', function() {
                        window.location.href = `/review/update/${review.id}`; /// review/update?review=${review.id}
                    });

                    // 삭제 버튼 생성
                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = 'ㅤ삭제ㅤ'
                    deleteButton.className = 'btn delete-review-btn';
                    deleteButton.addEventListener('click', function() {
                        deleteReview(review.id);
                    });

                    btnContainer.appendChild(editButton);
                    btnContainer.appendChild(deleteButton);
                } else {  // 로그인한 회원이 리뷰 작성자가 아닌 경우 공감 버튼 추가
                    const likeButton = document.createElement('button');
                    likeButton.className = 'btn like-review-btn';
                    if (review.likedByUser) {
                        likeButton.innerHTML = '<img src="/img/imgicon_Thumbs_On.png" class="like-button-img">' + `<span class="likes-count">${review.likesCount}</span>`;
                    } else {
                        likeButton.innerHTML = '<img src="/img/imgicon_Thumbs_Off.png" class="like-button-img">' + `<span class="likes-count">${review.likesCount}</span>`;
                    }

                    likeButton.addEventListener('click', function() {
                        // 로그인하지 않았을 경우
                        if (!loggedInUserNickname) {
                            if (confirm('리뷰 좋아요는 로그인이 필요합니다. 로그인 하시겠습니까?')) {
                                // 현재 페이지 URL 인코딩
                                //const redirectUrl = encodeURIComponent(window.location.href);
                                const redirectUrl = window.location.href;
                                window.location.href = `/member/detailLogin?redirect=${redirectUrl}`;
                            }
                        } else {
                            // 로그인 상태일 경우, 공감 기능 수행
                            handleReviewLikeClick(review.id, likeButton);
                        }
                    });
                    btnContainer.appendChild(likeButton);

                }





                reviewListContainer.appendChild(reviewElement);
            });

            // 리뷰 총 개수 업데이트
            const totalReviewCountElement1 = document.querySelector('.title.total-review');
            const totalReviewCountElement2 = document.querySelector('.evalCnt');
            totalReviewCountElement1.textContent = `${reviews.length}건의 방문자 평가`;
            totalReviewCountElement2.textContent = `(${reviews.length}건의 평가)`;

            // 평점 평균 계산 및 별점 표시
            const averageScore = reviews.length > 0 ? totalScoreSum / reviews.length : 0;
            const roundedAverageScore = Math.floor(averageScore * 10) / 10;

            const evalAvg1Element1 = document.querySelector('.evalAvg1');
            const evalAvg1Element2 = document.querySelector('.evalAvg2');
            evalAvg1Element1.textContent = `${roundedAverageScore}`;
            evalAvg1Element2.textContent = `${roundedAverageScore}`;

            displayAverageRatingStars(roundedAverageScore); // 별점 표시 함수

            // 카테고리별 평점 평균
            const flavorScoreAvgText = reviews.length > 0 ? (flavorScoreSum / reviews.length).toFixed(1) : "0";
            const priceScoreAvgText = reviews.length > 0 ? (priceScoreSum / reviews.length).toFixed(1) : "0";
            const serviceScoreAvgText = reviews.length > 0 ? (serviceScoreSum / reviews.length).toFixed(1) : "0";
            
            // 0일 경우 소수점 제거
            const formatScore = (score) => score.endsWith(".0") ? score.substring(0, score.length - 2) : score;

            const ratingCategoryAvgContainer = document.querySelector('.rating_category_avg');
            ratingCategoryAvgContainer.innerHTML = `
            <div class="review_scores_all">
                <span class="flavor_rating_avg">맛 <img src="/img/miniStar.png" class="miniStar">${formatScore(flavorScoreAvgText)}</span> 
                <span class="price_rating_avg">가격 <img src="/img/miniStar.png" class="miniStar">${formatScore(priceScoreAvgText)}</span>
                <span class="service_rating_avg">서비스 <img src="/img/miniStar.png" class="miniStar">${formatScore(serviceScoreAvgText)}</span>
            </div>`;


            // 리뷰 이미지 클릭 
            addReviewImageClickListener();
            
            if (reviews.length < 10) { // 요청한 페이지 크기보다 적은 리뷰가 반환된 경우, 마지막 페이지로 간주
                window.removeEventListener('scroll', onScroll); // 더 이상 스크롤 이벤트 리스너 필요 없음
            }
            
        } catch (error) {
            console.error('리뷰 목록을 불러오는 데 실패했습니다:', error);
        }
    }// -- reviewListLoad END --
    
    window.addEventListener('scroll', onScroll);
    

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


    // 리뷰 좋아요 버튼 클릭 
    async function handleReviewLikeClick(reviewId, likeButton) {
        // 좋아요 상태 확인
        const isLiked = likeButton.querySelector('img').src.includes('imgicon_Thumbs_On.png');

        try {
            let response; // 서버 응답 저장할 변수
            if (isLiked) {
                // 좋아요 삭제 요청
                response = await axios.delete(`/review/unlike/${reviewId}`);
            } else {
                // 좋아요 추가 요청
                response = await axios.post(`/review/likes`, { reviewId });
            }

            // 업데이트된 좋아요 개수 가져오기
            const updatedLikesCount = response.data.likesCount;

            // 좋아요 버튼 업데이트
            const newButtonHtml = isLiked
                ? `<img src="/img/imgicon_Thumbs_Off.png" class="like-button-img"><span class="likes-count">${updatedLikesCount}</span>`
                : `<img src="/img/imgicon_Thumbs_On.png" class="like-button-img"><span class="likes-count">${updatedLikesCount}</span>`;

            likeButton.innerHTML = newButtonHtml;
        } catch (error) {
            console.error('리뷰 좋아요 에러:', error);
        }
    }






    // 리뷰 이미지 클릭 이벤트 리스너 함수
    function addReviewImageClickListener() {
        document.querySelectorAll('.carousel-img').forEach(image => {
            image.addEventListener('click', function() {
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
        ratingStarContainer.innerHTML = ''; 

        // 두 번째 별점 컨테이너 선택
        const ratingStarContainer2 = document.querySelector('.rating_star2');
        ratingStarContainer2.innerHTML = ''; 

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

    // 리뷰 삭제
    async function deleteReview(reviewId) {
        if (!confirm("리뷰를 삭제하시겠습니까?")) return; 

        try {
            const response = await fetch(`/review/delete/${reviewId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            });

            if (response.ok) {
                alert("리뷰가 삭제되었습니다!");
                window.location.reload();
            } else {
                throw new Error('리뷰삭제실패');
            }
        } catch (error) {
            console.error('리뷰 삭제 중 에러 발생:', error);
            alert("리뷰 삭제에 실패했습니다.");
        }
    }



});//end document