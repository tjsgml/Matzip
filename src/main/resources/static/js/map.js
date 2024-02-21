/**
 * home.html
 *  
 * 지도를 불러와서 DB 안에 있는 음식점들 마커로 표시하기.
 */
document.addEventListener('DOMContentLoaded',async() =>{
	console.log('home.js...');
	
	//로그인 상태인지 확인
	isLoggedin = false;
	//회원의 아이디
	let memberId ='';
	
	checkLogin();
	
	const container = document.getElementById('map');
	const options = {
		center: new kakao.maps.LatLng(37.49822016965648, 127.02970793807465),
		level: 5
	};
		
	const map = new kakao.maps.Map(container, options);
	
	//마커를 저장할 배열
	const markers=[];
	
	
												
	// DB의 restaurant 테이블의 정보들을 불러오기...
	try{
		const response = await axios.get('/map/all');
		//console.log(response);
	    // DB에서 받은 음식점 정보를 이용하여 마커 생성
	    //리뷰들 가져오기.
	    response.data.forEach(restaurant => {
			const overlayPosition = new kakao.maps.LatLng(restaurant.lat, restaurant.lon);
			
			//리뷰 평점 가져오기
			getReviews(restaurant.id);
			
			//추천수 제일 높은 리뷰 가져오기.
			getMostLikedReview(restaurant.id);
			
			//음식점 좋아요 수 가져오기.
			getTotalMypicks(restaurant.id);
			
			//음식점 좋아요 상태 확인하기
			changeMyPick(restaurant.id);
			
            // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
            const content = `<div class="customoverlay" id="customoverlay-${restaurant.id}" data-id=${restaurant.id}>
                                <a  href="/rest/details?id=${restaurant.id}" id="v15_44" class="v15_44 btnMarker" type="button">
                                    <div class="v14_20"></div>
                                    <div class="v14_19"></div>
                                    <span class="v15_42">${restaurant.name}</span>
                                    <span class="v15_43">${restaurant.category.name}</span>
                                </a>
                            </div>`;

            // 커스텀 오버레이 생성
            const  customOverlay = new kakao.maps.CustomOverlay({
                map: map,
                position: overlayPosition,
                content: content,
                xAnchor: 0.15, // 기본값은 0.5, 오른쪽으로 이동하려면 작은 값으로 설정하면 됨.
				yAnchor: 1,
				zIndex:0
				
            });	
            
            //마커를 배열에 저장.
            markers.push({id: restaurant.id, overlay: customOverlay});
            
            const aa = document.getElementById(`customoverlay-${restaurant.id}`);
            //console.log(aa)
            aa.addEventListener('mouseover', () => {
				customOverlay.setZIndex(10);
				//console.log("들어옴")
			});
			document.getElementById(`customoverlay-${restaurant.id}`).addEventListener('mouseout', () => {
				customOverlay.setZIndex(0);
				//console.log("나감")
			});
             
			//--------------------------------------------------
			//지도 옆에 있는 음식점 리스트 그리기
			getRestList(response.data);	
					
		});
	}catch(error){
		console.log(error);
	}
				
//---------------함수들-------------------
	function getRestList(data){
		const restList = document.querySelector('div#restList');
			let htmlStr ='';
		data.forEach(restaurant => {
			
			htmlStr +=`
			<a id="restPost" class="restPost" href="/rest/details?id=${restaurant.id}" data-id="${restaurant.id}">
				<div id="post" data-id="${restaurant.id}">
					<div class="container" id="container" data-id="${restaurant.id}">
						<div class="item1" id="review-info-img-${restaurant.id}"></div>
						<div class="item2">${restaurant.name}</div>
						<div class="item3">${restaurant.category.name}</div>
						<div class="item4">
								<img style="margin-bottom: 7px; width: 20px;height: 20px;"src="/img/v26_89.png">
								<span id="review-info-grade-${restaurant.id}"></span>
								<div class="v-line"></div>
								<span class="button likeButton" id="likeButton" data-id="${restaurant.id}">
								    <img id="rest-myPick-${restaurant.id}" src="/img/v26_98.png" style="margin-bottom: 4px; width: 22px;height: 22px;">
								</span>
								<span id="rest-totalMyPick-${restaurant.id}">57<span>
						</div>
					</div>
					<div class="reviewDiv" id="review-info-${restaurant.id}" >
						<span class="reviewContent">“@@@@@@@@랜덤으로 뽑은 리뷰 내용@@@@@@@..”</span>
					</div>
					<div class="c1" id="review-c1-${restaurant.id}">
						<div class="i1">
							<span class="reviewLike" id="review-reviewLike-${restaurant.id}">21</span><span class="reviewLike2">명이 추천했습니다.</spanstyle>
						</div>
						<div class="i2">
							<span class="reviewNick" id="review-reviewNick-${restaurant.id}" >by.닉네임</span>
						</div>
					</div>
				</div>
			</a>
			   <hr style="margin:0;">
					`;
					
			
		});
		
		restList.innerHTML =htmlStr;
		
		// 모든 좋아요 버튼에 클릭 이벤트 핸들러 추가
		const likeButtons = document.querySelectorAll('span#likeButton');
		likeButtons.forEach((btn) => {
			btn.addEventListener('click',(event)=>{
				
			    event.preventDefault(); // 기본 동작 중지
			    if(isLoggedin ){
					 // 클릭된 버튼의 data-id 값을 가져옴
			        const restId = event.currentTarget.getAttribute('data-id');
			        
			        // 가져온 data-id 값을 사용하여 원하는 동작 수행
			        console.log("좋아요 버튼 클릭 - restId:", restId);
			        //좋아요 버튼 그리기
			        const btnMyPick = document.getElementById(`rest-myPick-${restId}`);
			        
			        if (btnMyPick.classList.contains('liked')) {//좋아요가 눌러져있는 상태
			        	console.log("좋아요 아이디:",btnMyPick.dataset.myPickId);
					    drawMyPick(btnMyPick.dataset.myPickId,restId);
					    btnMyPick.classList.remove('liked');
					    btnMyPick.src="/img/v26_98.png";
					} else {
					    drawMyPick(null,restId);
					    btnMyPick.classList.add('liked');
					    btnMyPick.src="/img/icon_myPickOn.png";
					}
		        }else{
					
				}
			});
		});	
		
		//음식점 게시물에 마우스를 올렸을 때 해당 지도 마커 변경 및 지도 이동,,,
			const restPost = document.querySelectorAll('a#restPost');
			restPost.forEach((post) =>{
				post.addEventListener('mouseout',turnOffMarker);
				post.addEventListener('mouseover',turnOnMarker);
			});
			
		
	}
	//게시물의 마우스를 올렸을 때 마커 색상, 포커스 변경
	function turnOnMarker(e) {
		//console.log("마우스 온");
		//console.log(e.currentTarget);
		const restPostId = e.currentTarget.getAttribute('data-id');
		//console.log("포스트 아이디:",restPostId);
		//console.log("마커 :",markers);
		const targetMarker = markers.find(marker => marker.id.toString() === restPostId);
		if(targetMarker){
			//console.log('마커 아이디:', targetMarker.id);
			const mk = document.getElementById(`customoverlay-${targetMarker.id}`);
			targetMarker.overlay.setZIndex(10);
			const latlng = targetMarker.overlay.getPosition();
			map.panTo(latlng);
			mk.querySelector('#v15_44').classList.add('mouseOn');
		}else{
			console.log('targetMarker를 찾을 수 없습니다.');
		}
	}
	//게시물의 마우스를 내렸을 때 마커 색상, 포커스 변경
	function turnOffMarker(e){
		//console.log("마우스 오프");
		//console.log(e.currentTarget);
		const restPostId = e.currentTarget.getAttribute('data-id');
		/*console.log("포스트 아이디:",restPostId);*/
		//console.log("마커 :",markers);
		const targetMarker = markers.find(marker => marker.id.toString() === restPostId);
		if(targetMarker){
			//console.log('마커 아이디:', targetMarker.id);
			const mk = document.getElementById(`customoverlay-${targetMarker.id}`);
			targetMarker.overlay.setZIndex(0);
			mk.querySelector('#v15_44').classList.remove('mouseOn');
		}else{
			console.log('targetMarker를 찾을 수 없습니다.');
		}
	}		
	
	//음식점들의 리뷰들 가져오기.-> 평점 수
	function getReviews(id) {
	    console.log('포스트 아이디:', id);
	    
	    $.ajax({
	        url: `/map/reviews/${id}`,
	        method: 'GET',
	        dataType: 'json',
	        success: function(response) {
	            const reviews = response;
	            if (reviews && reviews.length !== 0) {
	                let totalScore = 0;
	                reviews.forEach(rv => {
	                    totalScore += rv.flavorScore + rv.serviceScore + rv.priceScore;
	                    
	                    // 리뷰 이미지가 있는지 확인 후 이미지가 있다면 해당 이미지를 추가합니다.
	                   if (rv.reviewImages && rv.reviewImages.length > 0) {
	                        const reviewImgElement = document.getElementById(`review-info-img-${id}`);
	                        const firstImageUrl = rv.reviewImages[0]; // 첫 번째 이미지만 사용
	                        
	                        reviewImgElement.style.backgroundImage = `url('${firstImageUrl}')`;
	                
	                    }
	                });	
	                
	                const avgScore = (totalScore / (reviews.length * 3)).toFixed(1);
	                const totalReviews = reviews.length;
	                	
	                const reviewInfoElement = document.getElementById(`review-info-grade-${id}`);
	                reviewInfoElement.innerHTML = `${avgScore}(${totalReviews}명)`;
	            }else{
					const reviewInfoElement = document.getElementById(`review-info-grade-${id}`);
	                reviewInfoElement.innerHTML = `0(0명)`;
				}
	        },
	        error: function(xhr, status, error) {
	            console.error('Error fetching reviews:', error);
	        }
	    });
	}
	//공감수를 제일 많이 받은 리뷰 가져오기
	function getMostLikedReview(id){
		$.ajax({
		        url: `/map/getMostLikedReview/${id }`, // 리뷰 라이크가 가장 많은 리뷰를 가져오는 엔드포인트
		        type: 'GET',
		        dataType: 'json',
		        success: function(response) {
		            if(response && response.length > 0){//리뷰가 존재한다면,,
						let mostLikedReview = response[0];
						for(let i =1; i<response.length; i++){
							if(response[i].totalLikes > mostLikedReview.totalLikes){
								mostLikedReview = response[i];
							}
						}
						const reviewDiv = document.getElementById(`review-info-${id}`);
						const reviewContentSpan = reviewDiv.querySelector('.reviewContent');
						
						const reviewLike = document.getElementById(`review-reviewLike-${id}`);
						reviewLike.innerHTML=mostLikedReview.totalLikes;
						
						const reviewNick = document.getElementById(`review-reviewNick-${id}`);
						reviewNick.innerHTML = 'by.' + mostLikedReview.nickname;
						
						reviewContentSpan.innerHTML = '"'+mostLikedReview.content.slice(0,30)+'···"';
					}else{//리뷰가 없다면
						// 리뷰를 삭제할 요소를 찾습니다.
						const reviewDiv = document.getElementById(`review-info-${id}`);
						// 요소가 존재하면 삭제합니다.
						if (reviewDiv) {
						    reviewDiv.parentNode.removeChild(reviewDiv);
						}
						const reviewC1 = document.getElementById(`review-c1-${id}`);
						if (reviewDiv) {
						 reviewC1.parentNode.removeChild(reviewC1);
						}
						
						// 새로운 img 요소 생성
						const imgElement = document.createElement('img');
						
						// img 요소에 필요한 속성 설정
						imgElement.src = '/img/reviewNullImg.png'; // 이미지 경로 또는 URL 설정
						imgElement.alt = 'nullImg'; // 이미지 대체 텍스트 설정 (필요한 경우)
						imgElement.classList.add('reviewNullImg');
						
						// 원하는 요소에 img 요소 추가
						const reviewImgElement = document.getElementById(`review-info-img-${id}`);
						reviewImgElement.appendChild(imgElement);
						
						reviewImgElement.classList.add('nullImg');
					}
		        },
		        error: function(xhr, status, error) {
		            console.error('Error fetching most liked review:', error);
		        }
    	});
	}	
	//음식점 좋아요 수 가져오기.
	function getTotalMypicks(id){
		 $.ajax({
	        url: `/map/getTotalMypicks/${id}`, 
	        type: 'GET',
	        dataType: 'json',
	        success: function(response) {
	            console.log('음식점 좋아요 수:', response);
	            const totalMyPick = document.getElementById(`rest-totalMyPick-${id}`);
	            totalMyPick.innerHTML=response;
	        },
	        error: function(xhr, status, error) {
	            console.error('음식점 좋아요 수 가져오기 실패:', error);
	        }
	    });
	}
	//로그인 상태인지 아닌지 확인
	function checkLogin(){
				$.ajax({
			url:'/rest/details/checkMember',
			type:'GET',
			success:function(response){
				if(response !== null && response !== ''){
					//로그인 상태
					console.log('로그인 아이디 :',response);
					isLoggedin = true;
					memberId = response;
				}else{
					//로그아웃 상태
					console.log('로그아웃 상태');
				}
			},
			error:function(xhr, status,error){
				console.error('로그인 상태 확인 중 오류 발생 : ',error);
			}
		});
	}
	
	//회원의 해당 음식점에 좋아요를 눌렀는지 확인
	function changeMyPick(restId){
		if(isLoggedin){
			console.log('멤버 아이디:',memberId);
			console.log('음식점 아이디:',restId);
			
			$.ajax({
				url:'/rest/details/checkMyPick',
				type:'GET',
				data:{
					memberId: memberId,
					restId: restId
				},
				success:function(response){
					console.log('좋아요 아이디 :',response);
					if(response ==''){//좋아요가 안 눌러져있는 상태-> 그대로 둔다..
					
					}else{//좋아요가 눌러져 있는 상태 
						myPickId = response;
						isLiked=true;
						const btnMyPick = document.getElementById(`rest-myPick-${restId}`);
						btnMyPick.src="/img/icon_myPickOn.png";
						btnMyPick.classList.add('liked'); // 'liked' 클래스 추가(눌러져있는 상태 저장.)
						// myPickId를 dataset에 저장
	        			btnMyPick.dataset.myPickId = response;
					}
				},
				error: function(xhr, status, error) {
	            	console.error('MyPick 조회 중 오류 발생:', error);
	            }
				
			});
		}else{
			
		}
	}
	//좋아요 그리기 
	function drawMyPick(myPickId,restId){
		if(myPickId){//좋아요가 되어있는 상태-> 좋아요 취소-> 해당 행 삭제
			$.ajax({
				url:'/rest/details/myPick/'+ myPickId,
				type:'DELETE',
				
				success: function(response){
					console.log('취소완료');
					const btnMyPick = document.getElementById(`rest-myPick-${restId}`);
					btnMyPick.src="/img/v26_98.png";
					btnMyPick.classList.remove('liked'); // 'liked' 클래스 추가(눌러져있는 상태 저장.)
					// myPickId를 dataset에 저장
	    			btnMyPick.dataset.myPickId = null;
	    			getTotalMypicks(restId);
				},
				  error: function(xhr, status, error) {
		            console.error('좋아요 취소 중 오류 발생:', error);
		            // 오류 처리 로직을 추가하세요
		        }
			});
		}else{//좋아요 되어있지 않는 상태 -> 좋아요 누름 -> 행 삽입.
			const data ={memberId, restId};
			
			 axios.post('/rest/details/registerMyPick',data)
				  .then(response => {
					  console.log('좋아요 클릭시 아이디:',response.data);
					  console.log('좋아요 누름!');
					  
					  const btnMyPick = document.getElementById(`rest-myPick-${restId}`);
					  btnMyPick.src="/img/icon_myPickOn.png";
					  btnMyPick.classList.add('liked'); // 'liked' 클래스 추가(눌러져있는 상태 저장.)
					  // myPickId를 dataset에 저장
        			  btnMyPick.dataset.myPickId = response.data;
        			  getTotalMypicks(restId);
				  })
				  .catch(error=>{
					   console.error('오류 발생:', error);
				  });			
			
		}	
	}
	
});