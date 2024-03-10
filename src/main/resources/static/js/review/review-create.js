document.addEventListener("DOMContentLoaded", function() {
    
     document.getElementById("review").placeholder = 
     "방문한 식당의 솔직한 후기를 입력해 주세요!\n상세한 리뷰를 작성하고 좋아요를 받고 \n탑리뷰어에 도전해보세요!\n\n*부적절한 내용은 관리자에 의해 수정/삭제 될 수 있습니다.";
    
    /* 별 평점 */
    // 평가 카테고리 별 별점 값
    const ratingContainers = document.querySelectorAll(".rating-container");

    const tasteRating = parseInt(document.querySelector('#taste').getAttribute('data-rating'));
    const priceRating = parseInt(document.querySelector('#price').getAttribute('data-rating'));
    const serviceRating = parseInt(document.querySelector('#service').getAttribute('data-rating'));
    console.log(tasteRating, priceRating, serviceRating);


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
    let selectedFiles = [];

    // 이미 선택된 파일들을 DataTransfer 객체를 사용하여 input의 files 속성에 설정하는 함수
    function updateInputFiles(files) {
        const dataTransfer = new DataTransfer();
        files.forEach(file => {
            dataTransfer.items.add(file);
        });
        imageInput.files = dataTransfer.files;
    }

    // 이미지 미리보기에서 파일을 삭제하는 함수
    function removeFileFromInput(indexToRemove) {
        selectedFiles = selectedFiles.filter((_, index) => index !== indexToRemove);
        updateInputFiles(selectedFiles); // input의 files 속성 업데이트
        updateImagePreviews(selectedFiles); // 미리보기 업데이트
    }

    // 미리보기를 업데이트하는 함수
    function updateImagePreviews(files) {
        imagePreviewContainer.innerHTML = ''; // 기존 미리보기 클리어

        files.forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imagePreviewWrap = document.createElement('div');
                imagePreviewWrap.classList.add('image-preview-wrap');

                const imagePreview = document.createElement('img');
                imagePreview.src = e.target.result;

                // 삭제 버튼 추가
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'X';
                deleteButton.classList.add('delete-image');
                deleteButton.setAttribute('type', 'button'); // 폼 제출 방지
                deleteButton.onclick = function() {
                    removeFileFromInput(index); // 미리보기와 선택된 파일 목록에서 이미지 삭제
                };

                imagePreviewWrap.appendChild(imagePreview);
                imagePreviewWrap.appendChild(deleteButton); // 삭제 버튼을 미리보기에 추가

                imagePreviewContainer.appendChild(imagePreviewWrap);
            };
            reader.readAsDataURL(file);
        });
    }

    imageInput.addEventListener('change', function(event) {
        const newFiles = Array.from(event.target.files); // 사용자가 새롭게 선택한 파일들

        newFiles.forEach(file => {
            if (!selectedFiles.find(f => f.name === file.name && f.size === file.size)) {
                selectedFiles.push(file);
            }
        });

        updateInputFiles(selectedFiles); // input의 files 속성 업데이트
        updateImagePreviews(selectedFiles); // 미리보기 업데이트
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
    if (restaurantId) {
        const hiddenRestaurantIdInput = document.createElement('input');
        hiddenRestaurantIdInput.setAttribute('type', 'hidden');
        hiddenRestaurantIdInput.setAttribute('name', 'restaurantId');
        hiddenRestaurantIdInput.setAttribute('value', restaurantId);
        document.querySelector('form').appendChild(hiddenRestaurantIdInput);
    }

    function getQueryStringParams(query) {
        return new URLSearchParams(query);
    }



}); // end DOM