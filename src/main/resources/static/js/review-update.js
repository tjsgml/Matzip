document.addEventListener("DOMContentLoaded", function() {

/* 별 평점 */
    // 평가 카테고리 별 별점 값
    const ratingContainers = document.querySelectorAll(".rating-container");

    const tasteRating = parseInt(document.querySelector('#taste').getAttribute('data-rating'));
    const priceRating = parseInt(document.querySelector('#price').getAttribute('data-rating'));
    const serviceRating = parseInt(document.querySelector('#service').getAttribute('data-rating'));
    console.log(tasteRating, priceRating, serviceRating);
    
    // data-rating 값 읽고 별표 활성화 *****
    document.querySelectorAll('.rating-container').forEach(container => {
        const rating = parseInt(container.getAttribute('data-rating'));
        const stars = container.querySelectorAll('.star');
        stars.forEach((star, index) => {
            if (index < rating) {
                star.classList.add('active');
            }
        });
    });



    ratingContainers.forEach(container => {
        /*  */
        const stars = container.querySelectorAll(".star");

        stars.forEach(star => {
            star.addEventListener("click", function() {
                const value = parseInt(star.getAttribute("data-value"));

                // 클릭된 별표를 제외한 모든 별표에서 active 클래스 삭제
                stars.forEach(s => s.classList.remove("active"));

                // 클릭된 별표와 그 이전 별표들에 active 클래스 추가
                for (let i = 0; i < value; i++) {
                    stars[i].classList.add("active");
                }

                // 이 카테고리에 대한 별점 값 업데이트
                container.setAttribute("data-rating", value.toString());
                
                // 바로 업데이트된 data-rating 값을 사용하여 처리
                processRating(container.id, value);
            });

            star.addEventListener("mouseover", function() {
                const value = parseInt(star.getAttribute("data-value"));

                // 호버된 별표와 그 이전 별표들을 표시.
                for (let i = 0; i < value; i++) {
                    stars[i].classList.add("active");
                }
            });

            star.addEventListener("mouseout", function() {
                // 호버된 별표를 제외한 모든 별표에서 active 클래스 제거
                stars.forEach(s => s.classList.remove("active"));

                // 선택된 별표가 있으면, 그 별표와 이전 별표들 active 클래스 추가
                const rating = parseInt(container.getAttribute("data-rating"));
                if (rating > 0) {
                    for (let i = 0; i < rating; i++) {
                        stars[i].classList.add("active");
                    }
                }
            });
        });
    });
    
     // 카테고리 + 평점
    function processRating(categoryId, ratingValue) {
        console.log(`${categoryId} 평점: ${ratingValue}`);
        // hidden input 값 업데이트
        document.getElementById(`${categoryId}Rating`).value = ratingValue;
    }






    /* 이미지 */
    const imageInput = document.getElementById('image-input');
    const imagePreviewContainer = document.querySelector('.image-preview-container');
    const imagePreviewText = document.querySelector('.image-preview-text');

    imageInput.addEventListener('change', function() {

        if (this.files && this.files.length > 0) {
            imagePreviewText.remove();
        }

        for (const file of this.files) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imagePreviewWrap = document.createElement('div');
                imagePreviewWrap.classList.add('image-preview-wrap');

                const imagePreview = document.createElement('img');
                imagePreview.src = e.target.result;

                const deleteButton = document.createElement('a');
                deleteButton.innerHTML = '<i class="fas fa-times"></i>'; 
                deleteButton.classList.add('delete-image');
                deleteButton.href = 'javascript:void(0);';

                deleteButton.addEventListener('click', function() {
                    imagePreviewWrap.remove(); 
                });

                // 이미지 & 삭제 버튼 wrap에 추가
                imagePreviewWrap.appendChild(imagePreview);
                imagePreviewWrap.appendChild(deleteButton);

                // wrap을 imagePreviewContainer에 추가
                imagePreviewContainer.appendChild(imagePreviewWrap);
            };
            reader.readAsDataURL(file);
        }
    });
    
    

    
/* 키워드(해시태그) */
    // 태그 ID(삭제 시 필요)
    let tagIdCounter = 0;

    document.querySelectorAll('.tag-input').forEach(input => {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const tagValue = this.value.trim();
                if (tagValue) {
                    const category = this.getAttribute('data-category');
                    const tagId = `tag-${tagIdCounter++}`; // 태그 고유 ID 생성
    
                    // 숨겨진 입력 필드 생성
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    // name 속성 DTO 변수명과 일치하게 수정
                    let inputName = '';
                    if (category === 'visit-purpose') {
                        inputName = 'visitPurposeTags[]';
                    } else if (category === 'mood') {
                        inputName = 'moodTags[]';
                    } else if (category === 'convenience') {
                        inputName = 'convenienceTags[]';
                    }
                    hiddenInput.name = inputName;
                    hiddenInput.value = tagValue;
                    hiddenInput.id = tagId;
    
                    document.getElementById('reviewForm').appendChild(hiddenInput);
    
                    // 태그 UI 생성
                    const tagList = document.getElementById(input.getAttribute('data-category') + '-tags');
                    const tagItem = document.createElement('li');
                    tagItem.textContent = tagValue;
                    const deleteBtn = document.createElement('button');
                    deleteBtn.textContent = 'X';
                    deleteBtn.addEventListener('click', function() {
                        tagItem.remove(); // 태그 UI 삭제
                        document.getElementById(tagId).remove(); // 숨겨진 입력 필드 삭제
                    });
    
                    tagItem.appendChild(deleteBtn);
                    tagList.appendChild(tagItem);
    
                    this.value = ''; // 입력 필드 초기화
                }
            }
        });
    });


    
    
    document.querySelectorAll('.tag-input').forEach(function(input) {
        const maxLength = 8; // 글자 수 제한
        input.addEventListener('input', function() {
            if (this.value.length > maxLength) {
                // 글자수 제한 초과시 초과 부분 자름
                this.value = this.value.substring(0, maxLength);
                // 경고 표시
                displayWarning(this.name, '태그는 8글자를 초과할 수 없습니다.');
            } else {
                // 경고 숨김
                clearWarning(this.name);
            }
        });
    });


    //해시태그 글자수제한
    function displayWarning(inputName, message) {
        const warningElement = document.getElementById(inputName + '-warning');
        if (warningElement) {
            warningElement.textContent = message;
            warningElement.classList.remove('d-none');
        } else {
            // 경고요소 없으면
            const inputElement = document.querySelector(`input[name=${inputName}]`);
            const newWarning = document.createElement('small');
            newWarning.id = inputName + '-warning';
            newWarning.textContent = message;
            newWarning.classList.add('form-text', 'text-muted');
            inputElement.parentNode.insertBefore(newWarning, inputElement.nextSibling);
        }
    }
    function clearWarning(inputName) {
        const warningElement = document.getElementById(inputName + '-warning');
        if (warningElement) {
            warningElement.classList.add('d-none');
        }
    }

/* 폼 제출  */
    
        const reviewForm = document.getElementById("reviewForm");
        reviewForm.addEventListener("submit", function(e) {
            // 평점 입력 확인
            const tasteRating = document.getElementById('tasteRating').value;
            const priceRating = document.getElementById('priceRating').value;
            const serviceRating = document.getElementById('serviceRating').value;
            console.log(tasteRating, priceRating, serviceRating);
            
            if (tasteRating === "0" || priceRating === "0" || serviceRating === "0") {
                e.preventDefault();
                alert("모든 평점을 선택해주세요.");
                return;
            }
    
            const reviewContent = document.getElementById('review').value; 
            if (!reviewContent.trim()) {
                e.preventDefault();
                alert("리뷰 내용을 입력해주세요.");
                return;
            }
    
        });
    
    
    // 레스토랑 ID
    
    // URL에서 식당 ID 읽어오기
    const params = getQueryStringParams(window.location.search);
    const restaurantId = params.get('id'); // 'id'-쿼리 스트링 파라미터
    
    // 식당 ID hidden input 추가
    if(restaurantId) {
        const hiddenRestaurantIdInput = document.createElement('input');
        hiddenRestaurantIdInput.setAttribute('type', 'hidden');
        hiddenRestaurantIdInput.setAttribute('name', 'restaurantId');
        hiddenRestaurantIdInput.setAttribute('value', restaurantId);
        document.querySelector('form').appendChild(hiddenRestaurantIdInput);
    }
    
    function getQueryStringParams(query) {
        return new URLSearchParams(query);
    }










}); // end