/**
 * home.html
 *  
 * 지도를 불러와서 DB 안에 있는 음식점들 마커로 표시하기.
 */
document.addEventListener('DOMContentLoaded',async() =>{
	console.log('home.js...');
	
	var container = document.getElementById('map');
				var options = {
					center: new kakao.maps.LatLng(37.49822016965648, 127.02970793807465),
					level: 5
				};
		
				var map = new kakao.maps.Map(container, options);
												
				// DB의 restaurant 테이블의 정보들을 불러오기...
				try{
					const response = await axios.get('/api/map/all');
					console.log(response.data);
					
					    // DB에서 받은 음식점 정보를 이용하여 마커 생성
					    response.data.forEach(restaurant => {
									 const overlayPosition = new kakao.maps.LatLng(restaurant.lat, restaurant.lon);
				
				            // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
				            const content = `<div class="customoverlay" id="customoverlay-${restaurant.id}" data-id=${restaurant.id}>
				                                <div class="v15_44 btn btnMarker">
				                                    <div class="v14_20"></div>
				                                    <div class="v14_19"></div>
				                                    <span class="v15_42">${restaurant.name}</span>
				                                    <span class="v15_43">마라, 마라전골</span>
				                                </div>
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
				            
				            const aa = document.getElementById(`customoverlay-${restaurant.id}`);
				            console.log(aa)
				            aa.addEventListener('mouseover', () => {
								customOverlay.setZIndex(10);
								console.log("들어옴")
							});
							document.getElementById(`customoverlay-${restaurant.id}`).addEventListener('mouseout', () => {
								customOverlay.setZIndex(0);
								console.log("나감")
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
						<a id="restPost" href="/api/map/details?id=${restaurant.id}" id="restPost" data-id="${restaurant.id}">
								 <div class="v26_78" data-id="${restaurant.id}"> 
						        <div class="v26_79" data-id="${restaurant.id}"> </div>
						        <span class="v26_81" data-id="${restaurant.id}">${restaurant.name}</span>
						        <span class="v26_82">주요 메뉴명</span><span class="v26_83">태그들</span>
						        <div class="v26_89"></div>
								<span class="v26_90">평점(@@명)</span>
								<div class="name"></div>
						    <div class="v26_98"></div>
						    <span class="v26_99">좋아요수</span>
						    <span class="v26_101">“@@@@@@@@랜덤으로 뽑은 리뷰 내용@@@@@@@@@@@@..”</span>
						    <span class="v26_102">21명이 추천했습니다</span>
						    <span class="v26_103">by. 닉네임</span>
						   </div>
						</a>
						   <hr style="margin:0;">
								`;
					});
 					restList.innerHTML =htmlStr;
					//음식점 게시물에 마우스를 올렸을 때 해당 지도 마커 변경 및 지도 이동,,,
						const restPost = document.querySelectorAll('a#restPost');
						restPost.forEach((restPost) =>{
							restPost.addEventListener('mouseout',()=>{
								console.log("마우스 아웃");
							});
							restPost.addEventListener('mouseover',turnMarker);
						});
					
				}
				function turnMarker(e) {
					console.log("마우스 온");
					console.log(e.target);
					const restPostId = e.target.getAttribute('data-id');
					console.log(restPostId); 
				}
			
				
});