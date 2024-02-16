/**
 * home.html
 *  
 * 지도를 불러와서 DB 안에 있는 음식점들 마커로 표시하기.
 */
document.addEventListener('DOMContentLoaded',async() =>{
	console.log('home.js...');
	
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
		
	    // DB에서 받은 음식점 정보를 이용하여 마커 생성
	    response.data.forEach(restaurant => {
					 const overlayPosition = new kakao.maps.LatLng(restaurant.lat, restaurant.lon);

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
		<a id="restPost" href="/rest/details?id=${restaurant.id}" id="restPost" data-id="${restaurant.id}">
				 <div class="v26_78" data-id="${restaurant.id}"> 
		        <div class="v26_79" > </div>
		        <span class="v26_81" >${restaurant.name}</span>
		        <span class="v26_82">${restaurant.category.name}</span>
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
			restPost.addEventListener('mouseout',turnOffMarker);
			restPost.addEventListener('mouseover',turnOnMarker);
		});
	
}
function turnOnMarker(e) {
	console.log("마우스 온");
	console.log(e.target);
	const restPostId = e.target.getAttribute('data-id');
	console.log("포스트 아이디:",restPostId);
	console.log("마커 :",markers);
	const targetMarker = markers.find(marker => marker.id.toString() === restPostId);
	if(targetMarker){
		console.log('마커 아이디:', targetMarker.id);
		const mk = document.getElementById(`customoverlay-${targetMarker.id}`);
		console.log()
		targetMarker.overlay.setZIndex(10);
		const latlng = targetMarker.overlay.getPosition();
		map.panTo(latlng);
		mk.querySelector('#v15_44').classList.add('mouseOn');
	}else{
		console.log('targetMarker를 찾을 수 없습니다.');
	}
}
function turnOffMarker(e){
	console.log("마우스 오프");
	console.log(e.target);
	const restPostId = e.target.getAttribute('data-id');
	console.log("포스트 아이디:",restPostId);
	console.log("마커 :",markers);
	const targetMarker = markers.find(marker => marker.id.toString() === restPostId);
	if(targetMarker){
		console.log('마커 아이디:', targetMarker.id);
		const mk = document.getElementById(`customoverlay-${targetMarker.id}`);
		targetMarker.overlay.setZIndex(0);
		mk.querySelector('#v15_44').classList.remove('mouseOn');
	}else{
		console.log('targetMarker를 찾을 수 없습니다.');
	}
}		
				
});