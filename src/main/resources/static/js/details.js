/**
 * details.html 
 * 
 */

 document.addEventListener('DOMContentLoaded', async()=>{
	const mapModal = document.getElementById('mapModal');
	const btnShowModal = document.querySelector('button.showMap');
	let lat ='';
	let lon='';
	const restId = document.querySelector('input#restId').value;
	
	let isAdditionalMenuVisible = false; // 추가 메뉴가 보이는지 여부를 저장하는 변수
	//영업시간 정보들 가져오기
	findBusinessHour(restId);
	
	//메뉴 정보들 가져오기
	findMenu(restId);
	
	try{
		const response = await axios.get(`/rest/details/${restId}`);
		lon = response.data.lon;
		lat = response.data.lat;
	}catch(error){
		console.log(error);
	}
	
	const mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
	        level: 2 // 지도의 확대 레벨
	    };
	
	const map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	// 마커가 표시될 위치입니다 
	var markerPosition  = new kakao.maps.LatLng(lat, lon); 
	
	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
	    position: markerPosition
	});
	
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);
	resizeMap();
	map.relayout();
	
	btnShowModal.addEventListener('click',() => {
	toggleMap(true,markerPosition);
	console.log(mapModal);
	});
	
	
	
	
	//----------------함수들------------------------
	// 지도 표시하는 div 변경(모달창)
	function resizeMap() {
	    const mapContainer = document.getElementById('map');
	   	mapModal.style.display = 'true';
	    mapContainer.style.width = '100%';
	    mapContainer.style.height = '700px'; 
	}
	
	function relayout() {    
	    
	    resizeMap();
	    // 지도를 표시하는 div 크기를 변경한 이후 지도가 정상적으로 표출되지 않을 수도 있습니다
	    // 크기를 변경한 이후에는 반드시  map.relayout 함수를 호출해야 합니다 
	    // window의 resize 이벤트에 의한 크기변경은 map.relayout 함수가 자동으로 호출됩니다
	    map.relayout();
	}
	
	function toggleMap(active, position){
		if(active){
			mapModal.style.display='block';
			mapModal.className='';
			map.relayout();
			mapModal.className='modal';
			map.setCenter(position);
		}
	}
    //영업일 가져오기
	async function findBusinessHour(restId){
		console.log('findBusinessHour 호출');
		try{
			const response = await axios.get(`/rest/details/businessHour/${restId}`);
			console.log(response.data);
			
			//영업시간이 NULL 영업시간 정보가 아직 없다는 div 그리기,,,
			if (response.data.length === 0) {
            	drawBsHourNull();
	        } else {	
	            drawBusinessHour(response.data);
	        }
			
		}catch(error){
			console.log(error);
		}
	}	
	
	function drawBsHourNull(){
		const bsDiv = document.getElementById('businessDiv');
		
		console.log(bsDiv);
		
		bsDiv.innerHTML='';
		
		bsDiv.innerHTML +=`<div class="NullBsHour">
								<img src="/img/icon_sorry.png" style="display: inline-block;">
								<div class="bsHourNullString" >아직 정보가 없어요,,,</div>
						   </div>`;
	}
	
	function drawBusinessHour(data){
		console.log(data);
		const businessHoursForWeek = data;
		
		const dayOfWeek = ['MON','TUE','WED','THU','FRI','SAT','SUN'];
		
		dayOfWeek.forEach( day => {
			
			const bsHoursElementForDay = document.querySelector('.bs_hours#'+day);
			const businessHoursForDay = businessHoursForWeek.find(businessHour => businessHour.days === day);
			
				
				if(businessHoursForDay){
					 // 휴무 여부를 확인하고 처리합니다.
			        if (businessHoursForDay.isHoliday) {
			            const holidayImage = document.createElement('img');
			            holidayImage.src = '/img/icon_warning.png';
			
			            const holidayText = document.createElement('span');
			            holidayText.textContent = ' 휴무일';
			            holidayText.style.color = '#FF230A';
			            holidayText.style.borderColor = '#FF230A';
			
			            bsHoursElementForDay.appendChild(holidayImage);
			            bsHoursElementForDay.appendChild(holidayText);
						}else{
							  // 영업 시간을 추가합니다.
			            const openTimeSpan = document.createElement('span');
			            openTimeSpan.textContent = ' 영업시간 : ' + businessHoursForDay.openTime + ' ~ ';
			            const closeTimeSpan = document.createElement('span');
			            closeTimeSpan.textContent = businessHoursForDay.closeTime;
			
			            bsHoursElementForDay.appendChild(openTimeSpan);
			            bsHoursElementForDay.appendChild(closeTimeSpan);
						}
				}else{
				// 영업 정보가 없는 경우\
				console.log('정보없음');
                    const noInfoImage = document.createElement('img');
                    noInfoImage.src = '/img/icon_null1.png';
                    noInfoImage.style.marginBottom ='5px';

                    const noInfoText = document.createElement('span');
                    noInfoText.textContent = '정보가 없어요';
                    noInfoText.style.color = '#666670';
                    noInfoText.style.fontWeight = 'bold';
                    noInfoText.style.marginLeft = '5px';

                    bsHoursElementForDay.appendChild(noInfoImage);
                    bsHoursElementForDay.appendChild(noInfoText);
			}
			
		});//end dayOfWeek.forEach
		
	}//end function drawBusinessHour
	
	//메뉴 정보들 가져오기
	async function findMenu(restId){
		try{
			const response = await axios.get(`/rest/details/menu/${restId}`);
			if(response.data.length === 0){
				drawNullMenu();
			}else{
				drawMenus(response.data);
			}
		}catch(error){
			console.log(error);
		}
	}
	
	//메뉴 정보가 없으면 '정보 없음 페이지 보여주기'
	function drawNullMenu(){
		const menuDiv = document.getElementById('menuDiv');
		
		menuDiv.innerHTML='';
		
		menuDiv.innerHTML +=`<div class="NullBsHour">
								<img src="/img/icon_sorry.png" style="display: inline-block;">
								<div class="bsHourNullString" >아직 정보가 없어요,,,</div>
						   </div>`;
	}
	
	function drawMenus(data){
		console.log(data.length);
		
		const menuDiv = document.getElementById('menuDiv');
		
		// sales 컬럼이 큰 순서대로 데이터 정렬
     	data.sort((a, b) => b.sales - a.sales);
		
		if(data.length < 4){
			 // 메뉴들을 순회하면서 각 메뉴의 정보를 HTML에 추가
	        data.forEach((menu, index) => {
            let menuHTML = `<span class="menu-item">
                                <span class="menu-name">${menu.name}</span>
                                <hr class="hr-1"></hr>
                                <span class="menu-price">${menu.price}원</span>
                              </span>`;
            if (index < 2) {
                // 1위와 2위 메뉴에 아이콘 추가
                menuHTML = `<span class="menu-item">
                                <span class="menu-name">${menu.name}
                                <img src="/img/icon_best.png"  style="margin-bottom:6px;">
                                </span>
                                <hr class="hr-1"></hr>
                                <span class="menu-price">${menu.price}원</span>
                            </span>`;
            }
	            // 생성한 HTML을 메뉴 영역에 추가
	          menuDiv.innerHTML += menuHTML;
	        });
		}else{
			 // 메뉴들을 보여주는 함수 호출
			    drawInitialMenus(data.slice(0, 4)); // 처음 6개 메뉴만 표시
				console.log('호출');
			    // "더보기" 버튼 생성
			    const showMoreButtonSpan = document.getElementById('showMoreMenuSpan');
			    const showMoreButton = document.createElement('button');
			    showMoreButton.classList.add('showMoreMenu');
			    showMoreButton.setAttribute('id', 'showMoreMenu');
			    showMoreButton.innerHTML = `
			      더보기
			      <img src="/img/icon_down.png" style="margin-bottom:3px;">
			    `;
			    showMoreButtonSpan.appendChild(showMoreButton);
			
			    // "더보기" 버튼 클릭 시 추가 메뉴 표시
			    showMoreButton.addEventListener('click', function() {
			        if (isAdditionalMenuVisible) {
				        // 추가 메뉴가 보이는 상태라면 숨기고 상태를 변경합니다.
				        showMoreButton.innerHTML='';
				        showMoreButton.innerHTML=' 더보기 <img src="/img/icon_down.png" style="margin-bottom:3px;">';
				        drawInitialMenus(data.slice(0, 4));
				        isAdditionalMenuVisible = false;
				    } else {
				        // 추가 메뉴가 보이지 않는 상태라면 보이게 하고 상태를 변경합니다.
				        drawMoreMenus(data.slice(4)); 
				        showMoreButton.innerHTML='';
				        showMoreButton.innerHTML=' 접기 <img src="/img/icon_up.png" style="margin-bottom:3px;">';
				        isAdditionalMenuVisible = true;
				    }
			    });
		}
		
	}
	// 처음 메뉴들을 보여주는 함수
	function drawInitialMenus(data) {
	    const menuDiv = document.getElementById('menuDiv');
		menuDiv.innerHTML='';
	    data.forEach((menu, index) => {
            let menuHTML = `<span class="menu-item">
                                <span class="menu-name">${menu.name}</span>
                                <hr class="hr-1"></hr>
                                <span class="menu-price">${menu.price}원</span>
                              </span>`;
            if (index < 2) {
                // 1위와 2위 메뉴에 아이콘 추가
                menuHTML = `<span class="menu-item">
                                <span class="menu-name">${menu.name}
                                <img src="/img/icon_best.png" style="margin-bottom:6px;">
                                </span>
                                <hr class="hr-1"></hr>
                                <span class="menu-price">${menu.price}원</span>
                            </span>`;
            }
	        // 생성한 HTML을 메뉴 영역에 추가
	        menuDiv.innerHTML += menuHTML;
	    });
	}
	// 추가 메뉴를 보여주는 함수
		function drawMoreMenus(data) {
		    const menuDiv = document.getElementById('menuDiv');
		    data.forEach(menu => {
		        // 메뉴의 정보를 HTML 문자열로 생성
		        const menuHTML = `<span class="menu-item">
		                            <span class="menu-name">${menu.name}</span>
		                            <hr class="hr-1"></hr>
		                            <span class="menu-price">${menu.price}원</span>
		                          </span>`;
		        // 생성한 HTML을 메뉴 영역에 추가
		        menuDiv.innerHTML += menuHTML;
		    });
		}

	

 });//end document