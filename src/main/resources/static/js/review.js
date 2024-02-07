const ratingStars = [...document.getElementsByClassName("rating_star")];

function executeRating(stars){
  const starClassActive = "rating_star fas fa-star"; // 선택된 별
  const starClassInactive = "rating_star far fa-star"; // 선택되지 않은 별
  const starLength = stars.length; // 별 요소를 담은 배열 길이 읽으려고
  let i; // 루프에서 씀

  /**
   * stars배열에서 각 요소에 대해 클릭 확인.
   * 별 클릭 시 별을 선택된 상태로 바꿈.
   * i 는 클릭된 별의 인덱스
   * if문으로 클릭된 별이 빈 별인지 확인
   *    (t: 배열의 처음부터 클릭된 요소까지 선택됨표시, 
   *     f: 클릭한 별 부터 별 배열 끝까지 빈 별로 표시)
   */
  stars.map((star) => {
    star.onclick=() => {
      i = stars.indexOf(star);
      
      if(star.className === starClassInactive){
        for(i; i >=0; --i) stars[i].className = starClassActive;
      } else {
        for(i; i < starLength; ++i) stars[i].className = starClassInactive;
      }
    }; 
  
  });

} // end executeRating

executeRating(ratingStars);


