document.addEventListener("DOMContentLoaded", function() {
    initializeStarRatings();
    console.log(hashtags);
    
    
    
    
    
    // 수정 중 페이지 벗어날때 경고창
    window.addEventListener('beforeunload', function(e) {
        // 변경사항이 있을 때 경고를 표시.(여기에 변경사항이 있는지 검사하는 로직 추가하기.'폼 데이터가 처음 로드될 때 상태랑 다른지 비교 검사'
        // 일단 평점 변경시에는 창 뜸
        e.returnValue = '수정을 취소하시겠습니까? 수정한 내용은 저장되지 않습니다.'; // 대부분의 브라우저는 기본메시지 사용. e.returnValue에 값 설정해도 사용자 정의 메시지는 표시되지 않을 수 있음.
        return e.returnValue;
    });
    
    



/* 이미지 */
    const imageInput = document.getElementById('image-input');
    const imagePreviewContainer = document.querySelector('.image-preview-container');
    let selectedFiles = [];
    let deleteImageRequests = [];
    
    // HTML에서 th:inline="javascript"를 통해 전달받은 reviewImages 사용
    if (window.reviewImages && window.reviewImages.length > 0) {
        updateImagePreviewsWithExistingImages(window.reviewImages);
    }
    
    // 서버에 저장된 이미지 미리보기에 추가
    function updateImagePreviewsWithExistingImages(imageUrls) {
        const uploadText = document.querySelector('.image-preview-text');
        if (uploadText) {
            uploadText.style.display = 'none'; // 이미지가 있으므로 텍스트를 숨깁니다.
        }
    
        imageUrls.forEach((imageUrl, index) => {
            const imagePreviewWrap = document.createElement('div');
            imagePreviewWrap.classList.add('image-preview-wrap');
    
            const imagePreview = document.createElement('img');
            imagePreview.src = imageUrl;
    
            // 삭제 버튼 추가
            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'X';
            deleteButton.classList.add('delete-image');
            deleteButton.setAttribute('type', 'button'); // 폼 제출 방지
            deleteButton.onclick = function() {
                const confirmed = confirm("해당 이미지를 삭제하시겠습니까?\n" + imageUrl);
                if (confirmed) {
                    // 서버에서 이미지 삭제 요청 로직을 여기에 추가
                    deleteImageRequests.push(imageUrl);
                    imagePreviewWrap.remove(); // 미리보기에서 이미지 삭제
                    // 모든 이미지가 삭제되었는지 확인하고 "이미지 업로드" 텍스트 다시 표시
                    if (imagePreviewContainer.querySelectorAll('.image-preview-wrap').length === 0) {
                        if (uploadText) {
                            uploadText.style.display = 'block';
                        }
                    }
                }
            };
    
            imagePreviewWrap.appendChild(imagePreview);
            imagePreviewWrap.appendChild(deleteButton); // 삭제 버튼을 미리보기에 추가
    
            imagePreviewContainer.appendChild(imagePreviewWrap);
        });
    }

    
    

    
    // 이미 선택된 파일들 DataTransfer 객체 사용해서 input files 속성에 설정
    function updateInputFiles(files) {
        const dataTransfer = new DataTransfer();
        files.forEach(file => {
            dataTransfer.items.add(file);
        });
        imageInput.files = dataTransfer.files;
    }

    // 이미지 미리보기에서 파일 삭제
    function removeFileFromInput(indexToRemove) {
        selectedFiles = selectedFiles.filter((_, index) => index !== indexToRemove);
        updateInputFiles(selectedFiles); // input의 files 속성 업데이트
    }

    // 미리보기를 업데이트하는 함수
    function updateImagePreviews(files , isNewFile = false) {
        // 이미지 업데이트하면 "이미지 업로드" 텍스트 숨김
        const uploadText = document.querySelector('.image-preview-text');
        if (uploadText) {
            uploadText.style.display = 'none';
        }
    
        files.forEach((file, index) => {
            if(isNewFile && document.querySelector(`[data-name="${file.name}"]`)) {
                // 이미 미리보기가 존재하는 파일은 건너뛰기
                return;
            }
            const reader = new FileReader();
            reader.onload = function(e) {
                const imagePreviewWrap = document.createElement('div');
                imagePreviewWrap.classList.add('image-preview-wrap');
                imagePreviewWrap.setAttribute('data-name', file.name); // 파일 이름으로 데이터속성 추가
    
                const imagePreview = document.createElement('img');
                imagePreview.src = e.target.result;
    
                // 삭제 버튼 추가
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'X';
                deleteButton.classList.add('delete-image');
                deleteButton.setAttribute('type', 'button'); // 폼 제출 방지
                deleteButton.onclick = function() {
                    // 선택된 파일 목록에서 이미지 삭제
                    removeFileFromInput(index);
                    // 미리보기에서 이미지 삭제
                    imagePreviewWrap.remove();
                    // 미리보기 이미지 다 삭제되면 "이미지 업로드" 텍스트 다시 표시
                    if (imagePreviewContainer.querySelectorAll('.image-preview-wrap').length === 0) {
                        if (uploadText) {
                            uploadText.style.display = 'block';
                        }
                    }
                };
    
                imagePreviewWrap.appendChild(imagePreview);
                imagePreviewWrap.appendChild(deleteButton); // 삭제 버튼 미리보기에 추가
    
                imagePreviewContainer.appendChild(imagePreviewWrap);
            };
            reader.readAsDataURL(file);
        });
    }


    imageInput.addEventListener('change', function(event) {
        // 파일이 선택됐는지 확인
        if (event.target.files.length === 0) {
            return;
        }
    
        const newFiles = Array.from(event.target.files); // 새로 선택한 파일들
    
        newFiles.forEach(file => {
            if (!selectedFiles.find(f => f.name === file.name && f.size === file.size)) {
                selectedFiles.push(file);
                // 새로 추가된 파일 미리보기 업뎃
                updateImagePreviews([file], true); // -> true는 새파일
            }
        });
    
        updateInputFiles(selectedFiles); // input의 files 속성 업데이트
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
    
    
    hashtags.forEach(hashtag => {
        // 'hashtagCategory' 필드를 사용하여 태그 리스트 선택
        const tagList = document.getElementById(hashtag.hashtagCategory + '-tags');
        if (!tagList) {
            console.error('Tag list not found for category:', hashtag.hashtagCategory);
            return; // 해당 카테고리의 태그 리스트가 없으면 다음 해시태그로 넘어감
        }
    
        const tagItem = document.createElement('li');
        tagItem.textContent = hashtag.keyword;
    
        // 숨겨진 입력 필드 생성 (해시태그 ID 저장 용도)
        const hiddenInput = document.createElement('input');
        hiddenInput.setAttribute('type', 'hidden');
        hiddenInput.setAttribute('name', 'hashtags[]');
        hiddenInput.value = hashtag.hashtagId;
        tagItem.appendChild(hiddenInput);
    
        // 삭제 버튼 추가
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'X';
        deleteButton.addEventListener('click', function() {
            tagItem.remove(); // 태그 아이템 삭제
            // 서버에 삭제 요청 로직 구현 필요
        });
    
        tagItem.appendChild(deleteButton);
        tagList.appendChild(tagItem);
    });





/*
 * 함수들 
 */ 
    function initializeStarRatings() {
        document.querySelectorAll(".rating-container").forEach(container => {
            let currentRating = parseInt(container.getAttribute('data-rating'));
            updateStars(container, currentRating);
    
            container.querySelectorAll(".star").forEach(star => {
                star.addEventListener("mouseover", () => highlightStarsOnHover(container, star));
                star.addEventListener("mouseout", () => updateStars(container, currentRating));
                star.addEventListener("click", () => {
                    currentRating = parseInt(star.getAttribute('data-value'));
                    container.setAttribute('data-rating', currentRating.toString());
                    updateStars(container, currentRating);
                    updateHiddenInput(container.id, currentRating);
                });
            });
        });
    }
    
    // 별 평점 마우스 오버시 active
    function highlightStarsOnHover(container, hoveredStar) {
        const hoverRating = parseInt(hoveredStar.getAttribute('data-value'));
        container.querySelectorAll(".star").forEach(star => {
            const starValue = parseInt(star.getAttribute('data-value'));
            star.classList.toggle("active", starValue <= hoverRating);
        });
    }
    
    // 별 평점 클릭시 해당 값으로 active 상태 변경
    function updateStars(container, rating) {
        container.querySelectorAll(".star").forEach(star => {
            const starValue = parseInt(star.getAttribute('data-value'));
            star.classList.toggle("active", starValue <= rating);
        });
    }
    
    // 별 평점 클릭시 hidden Input 값 변경
    function updateHiddenInput(containerId, rating) {
        const ratingInput = document.getElementById(`${containerId}Rating`);
        if (ratingInput) {
            ratingInput.value = rating;
        }
    }
    
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

    
}); // end DOM