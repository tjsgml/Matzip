document.addEventListener("DOMContentLoaded", function() {

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
                
                // 바로 업데이트된 data-rating 값을 사용하여 처리 (예: 콘솔에 출력)
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
    
     // 평점 처리: 카테고리 ID, 평점 값 
    function processRating(categoryId, ratingValue) {
        console.log(`${categoryId} 평점: ${ratingValue}`);
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
                deleteButton.innerHTML = '<i class="fas fa-times"></i>'; // Font Awesome 사용
                deleteButton.classList.add('delete-image');
                deleteButton.href = 'javascript:void(0);';

                deleteButton.addEventListener('click', function() {
                    imagePreviewWrap.remove(); // 이 부분이 이미지와 삭제버튼을 감싼 div를 제거합니다.
                });

                // 이미지와 삭제 버튼을 wrap에 추가
                imagePreviewWrap.appendChild(imagePreview);
                imagePreviewWrap.appendChild(deleteButton); // 이 부분을 수정했습니다.

                // wrap을 imagePreviewContainer에 추가
                imagePreviewContainer.appendChild(imagePreviewWrap); // 수정했습니다.
            };
            reader.readAsDataURL(file);
        }
    });
    
    
    /* 키워드(해시태그) */
    document.querySelectorAll('.tag-input').forEach(function(input) {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault(); // 폼 제출 방지
                const tagValue = this.value.trim(); // 입력된 값 가져오기
                console.log(tagValue);
                if (tagValue) {
                    const category = this.getAttribute('data-category'); // 해당 입력 필드의 카테고리 가져오기
                    const tagList = document.getElementById(category); // 카테고리의 태그리스트 가져오기
                    const newTag = document.createElement('li'); // 새 태그 만듦
                    newTag.textContent = '#' + tagValue; // 텍스트 내용 설정
                    tagList.appendChild(newTag); // 태그 리스트에 새 태그 추가
                    this.value = ''; // 입력 필드 초기화
                }
            }
        });
    });




    
    





}); // end
