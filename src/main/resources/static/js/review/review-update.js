document.addEventListener("DOMContentLoaded", function() {
    // 별점 초기화
    //initializeStarRatings();

    // 이미지 미리보기 초기화
    updateImagePreviews();

    // 해시태그 입력 초기화
    initializeTags();

    // 폼 제출 이벤트 핸들러
    const reviewForm = document.getElementById("reviewForm");
    reviewForm.addEventListener("submit", handleSubmit);

    // 레스토랑 ID 초기화
    initializeRestaurantId();
});

/* 별 평점 초기화 */
function initializeStarRatings() {
    const ratingContainers = document.querySelectorAll(".rating-container");
    ratingContainers.forEach(container => {
        const stars = container.querySelectorAll(".star");
        const currentRating = parseInt(container.getAttribute('data-rating'), 10);
        updateStars(stars, currentRating);

        stars.forEach(star => {
            star.addEventListener("click", handleStarClick);
            star.addEventListener("mouseover", handleStarMouseover);
            star.addEventListener("mouseout", () => updateStars(stars, currentRating));
        });
    });
}

function handleStarClick(event) {
    const star = event.target;
    const container = star.closest('.rating-container');
    const stars = container.querySelectorAll('.star');
    const ratingValue = parseInt(star.getAttribute('data-value'), 10);
    container.setAttribute('data-rating', ratingValue.toString());
    updateStars(stars, ratingValue);

    // 평점 값 업데이트
    const categoryId = container.id; // taste, price, service 중 하나
    document.querySelector(`input[name="${categoryId}Rating"]`).value = ratingValue;
}

function handleStarMouseover(event) {
    const star = event.target;
    const container = star.closest('.rating-container');
    const stars = container.querySelectorAll('.star');
    const hoverValue = parseInt(star.getAttribute('data-value'), 10);
    updateStars(stars, hoverValue);
}

function updateStars(stars, rating) {
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add("active");
        } else {
            star.classList.remove("active");
        }
    });
}

/* 이미지 미리보기 초기화 */
function updateImagePreviews() {
    // 기존 업로드한 이미지를 서버로부터 불러와서 미리보기에 표시하는 코드를 여기에 추가하세요.
}

/* 해시태그 입력 초기화 */
function initializeTags() {
    // 서버로부터 불러온 기존 해시태그를 입력 필드에 표시하는 코드를 여기에 추가하세요.
}

function handleSubmit(e) {
    // 폼 제출 전에 필요한 검증을 수행하는 코드를 여기에 추가하세요.
}

/* 레스토랑 ID 초기화 */
function initializeRestaurantId() {
    // URL에서 식당 ID를 읽어와서 hidden input에 설정하는 코드를 여기에 추가하세요.
}
