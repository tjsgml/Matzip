/**
 * details.html 
 * 
 */

 document.addEventListener('DOMContentLoaded', async()=>{
     //현재 페이지 저장
    const redirectUrl = encodeURIComponent(window.location.href);
     
    const mapModal = document.getElementById('mapModal');
    const btnShowModal = document.getElementById('showMap');
    let lat ='';
    let lon='';
    //좋아요 상태(false - 좋아요X, true - 좋아요O )
    let isLiked = false;
    //로그인 상태인지 확인
    let isLoggedin = false;
    //회원의 아이디
    let memberId ='';
    //좋아요를 한 상태라면 좋아요 아이디
    let myPickId = '';
     
    checkLogin();
    
    
    const restId = document.querySelector('input#restId').value;//음식점 아이디
    
    fillRestImg(restId);
    
    const btnReportInfo = document.getElementById('btnReportInfo');//폐업신고,정보수정제안 버튼
    //좋아요 버튼
    const btnMyPick = document.getElementById('btnMyPick');
    
    // 평가하기 버튼 클릭 이벤트 수정
    const btnEval = document.getElementById('btnEval');
    

    //--------------------------------------------------------------------------------------------------------

    
    btnEval.addEventListener('click', () => {
        if(isLoggedin){
            // 로그인 상태인 경우, 리뷰 작성 페이지로 이동하면서 레스토랑 ID 전달
            const restaurantId = new URL(window.location.href).searchParams.get("id");
            window.location.href = `/review/create?restaurantId=${restaurantId}`;
        } else {
            // 로그인이 필요한 경우
            if (confirm('로그인이 필요합니다. 로그인 하시겠습니까?')) {
                
                // 현재 페이지의 URL을 가져와서 로그인 페이지로 이동하면서 전달
                window.location.href = `/member/detailLogin?redirect=${redirectUrl}`;
            } else {
                alert('로그인이 취소되었습니다.');
            }
        }
    });

                    

    //폐업신고,정보수정제안 버튼 클릭시 
    btnReportInfo.addEventListener('click',() => {
        const textReportInfo = document.getElementById('reportInfo').value;
        if(textReportInfo==''){
            alert('수정할 내용을 입력해주세요.');
        }else{
            const reportInfo =document.getElementById('reportInfo');
            
            updateInfo(textReportInfo);
            
            alert('전송 완료. 소중한 의견 감사합니다!');
            $('#reportModal').modal('hide');
            
            
            //모달 텍스트창 초기화.
            reportInfo.value='';
        }
    });
    
    //좋아요 버튼 클릭시
    btnMyPick.addEventListener('click',() => {
        //(1) 로그인 상태 확인
        //(2) 로그인 상태가 아니면 팝업창을 보여주고 확인 시, 로그인 페이지로 넘기기.
        if(isLoggedin){
            console.log('클릭');
            drawMyPick();
        }else{
             if (confirm('로그인이 필요합니다. 로그인 하시겠습니까?')) {
                    // 로그인 페이지로 이동
                     window.location.href = `/member/detailLogin?redirect=${redirectUrl}`;
                   /* window.location.href = '/member/login';*/
                } else {
                    // 사용자가 취소한 경우
                    alert('로그인이 취소되었습니다.');
                }
        }
    });
    

    
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
    
    //지도보기 버튼 클릭시
    btnShowModal.addEventListener('click',() => {
    toggleMap(true,markerPosition);
    console.log(mapModal);
    });
    
    
    
    
    //----------------함수들------------------------
    // 지도 표시하는 div 변경(모달창)
    function resizeMap() {
        const mapContainer = document.getElementById('map');
        mapModal.style.display = 'true';
        mapContainer.style.width = '798px';
        mapContainer.style.height = '500px'; 
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
        }else{
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
                    changeMyPick(memberId,restId);
                }else{
                    //로그아웃 상태

                }
            },
            error:function(xhr, status,error){
                console.error('로그인 상태 확인 중 오류 발생 : ',error);
            }
        });
    }
    //회원의 해당 음식점에 좋아요를 눌렀는지 확인
    function changeMyPick(memberId,restId){
        
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
                if(response ==''){
                }else{
                    myPickId = response;
                    isLiked=true;
                    btnMyPick.classList.add('liked');
                }
            },
            error: function(xhr, status, error) {
                console.error('MyPick 조회 중 오류 발생:', error);
            }
            
        });
    }
    //좋아요 그리기 
    function drawMyPick(){
        if(isLiked){//좋아요가 되어있는 상태-> 좋아요 취소-> 해당 행 삭제
            $.ajax({
                url:'/rest/details/myPick/'+ myPickId,
                type:'DELETE',
                beforeSend: function(xmlHttpRequest){
                    xmlHttpRequest.setRequestHeader("AJAX","true");
                },
                success: function(response){
                    console.log('좋아요 :',response);
                    console.log('취소완료');
                    btnMyPick.classList.remove('liked');
                    isLiked = false;
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
                      console.log('좋아요 :',response);
                      console.log('좋아요 클릭시 아이디:',response.data);
                      console.log('좋아요 누름!');
                      btnMyPick.classList.add('liked');
                      myPickId= response.data;
                      isLiked = true;
                  })
                  .catch(error=>{
                       console.error('오류 발생:', error);
                  });           
            
        }   
    }
    //폐업신고, 정보 수정 제안 메시지 보내기
    function updateInfo(content){
        const data ={restId,content};
        
        const response = axios.post('/rest/details/update',data);
        
    }
    
    //이미지 사진들 채우기-----------------------------------
    async function fillRestImg(restId){
        let imgNum = 0;
        let imgArray =[];
          try {
                const response = await axios.get(`/rest/details/reviews/${restId}`);
                const reviews = response.data;
                
                reviews.forEach((rv)=>{
                    if(rv.reviewImages.length > 0){//리뷰 이미지가 한개라도 있으면 
                    console.log('이미지:',rv.reviewImages);
                        rv.reviewImages.forEach((img)=>{
                             imgArray.push(img);
                             imgNum++; 
                        });
                    }
                    
                });
                console.log("이미지 개수:",imgNum);
                console.log("이미지 배열:",imgArray);
                
                // 그리드 레이아웃 조정
                const container = document.querySelector('.container');
                let numColumns = 4;
                let numRows = 2;
                
                if (imgNum > 1 && imgNum <= 4 && imgNum != 3) {
                    numColumns = 4;
                    numRows = 2;
                } else if ( imgNum ==3) {
                    numColumns = 3;
                    numRows = 2;
                }else if(imgNum === 5){
                     numColumns = 8;
                    numRows = 2;
                }else if (imgNum >= 6) {
                    numColumns = 3;
                    numRows = 2;
                    imgNum=6;
                }
                
                container.style.gridTemplateColumns = `repeat(${numColumns}, 1fr)`;
                container.style.gridTemplateRows = `repeat(${numRows}, 1fr)`;
                
                switch(imgNum){
                    case 0://총 이미지 개수가 1개일 때
                        container.remove();
                    
                    break;
                    case 1://총 이미지 개수가 1개일 때
                        for (let i = 0; i < imgNum; i++) {
                        let imgBox = document.createElement('div');
                        imgBox.classList.add('box');
                        imgBox.dataset.index = i;
                        imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                        imgBox.style.backgroundSize = 'cover';
                        imgBox.style.backgroundPosition ='center center';
                        imgBox.style.gridColumn = "2 / 4";
                        imgBox.style.gridRow = "1/3";
                        container.appendChild(imgBox);
                    }
                    break;
                    case 2://총 이미지 개수가 2개일 때.
                         imgBox = document.createElement('div');
                            for (let i = 0; i < imgNum; i++) {
                            let imgBox = document.createElement('div');
                            imgBox.classList.add('box');
                            imgBox.dataset.index = i;
                            
                            switch (i) {
                                case 0:
                                    imgBox.style.gridColumn = "1 / 3";
                                    imgBox.style.gridRow = "1/3";
                                    break;
                                case 1:
                                    imgBox.style.gridColumn = "3 / 5";
                                    imgBox.style.gridRow = "1 / 3 ";
                                    break;
                            }
                            
                            imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                            imgBox.style.backgroundSize = 'cover';
                            imgBox.style.backgroundPosition ='center center';
                            container.appendChild(imgBox);
                        }
                    break;
                    case 3://총 이미지 개수가 3개일 때.
                         imgBox = document.createElement('div');
                            for (let i = 0; i < imgNum; i++) {
                            let imgBox = document.createElement('div');
                            imgBox.classList.add('box');
                            imgBox.dataset.index = i;
                            
                            switch (i) {
                                case 0:
                                    imgBox.style.gridColumn = "1 / 3";
                                    imgBox.style.gridRow = "1/3";
                                    break;
                                case 1:
                                    imgBox.style.gridColumn = "3 / 5";
                                    break;
                                case 2:
                                    imgBox.style.gridColumn = "3 / 5";
                                    break;
                            }
                            
                            imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                            imgBox.style.backgroundSize = 'cover';
                            container.appendChild(imgBox);
                        }
                    break;
                    case 4://총 이미지 개수가 4개일 때.
                         imgBox = document.createElement('div');
                            for (let i = 0; i < imgNum; i++) {
                            let imgBox = document.createElement('div');
                            imgBox.classList.add('box');
                            imgBox.dataset.index = i;
                            
                            switch (i) {
                                case 0:
                                    imgBox.style.gridColumn = "1 / 3";
                                    imgBox.style.gridRow = " 1 / 2";
                                    break;
                                case 1:
                                    imgBox.style.gridColumn = "3 / 5";
                                    break;
                                case 2:
                                    imgBox.style.gridColumn = "1 / 3";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                                case 3:
                                    imgBox.style.gridColumn = "3 / 5";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                            }
                            
                            imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                            imgBox.style.backgroundSize = 'cover';
                            imgBox.style.backgroundPosition ='center center';
                            container.appendChild(imgBox);
                        }
                    break;
                    case 5://총 이미지 개수가 5개일 때.
                         imgBox = document.createElement('div');
                            for (let i = 0; i < imgNum; i++) {
                            let imgBox = document.createElement('div');
                            imgBox.classList.add('box');
                            imgBox.dataset.index = i;
                            
                            switch (i) {
                                case 0:
                                    imgBox.style.gridColumn = "1 / 5";
                                    imgBox.style.gridRow = " 1 / 3";
                                    break;
                                case 1:
                                    imgBox.style.gridColumn = "5 / 7";
                                    imgBox.style.gridRow = " 1 / 2";
                                    break;
                                case 2:
                                    imgBox.style.gridColumn = "7 / 9";
                                    imgBox.style.gridRow = " 1 / 2";
                                    break;
                                case 3:
                                    imgBox.style.gridColumn = "5 / 7";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                                case 4:
                                    imgBox.style.gridColumn = "7 / 9";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                            }
                            
                            imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                            imgBox.style.backgroundSize = 'cover';
                            imgBox.style.backgroundPosition ='center center';
                            container.appendChild(imgBox);
                        }
                    break;
                    case 6://총 이미지 개수가 5개일 때.
                         imgBox = document.createElement('div');
                            for (let i = 0; i < imgNum; i++) {
                                let imgBox = document.createElement('div');
                                imgBox.classList.add('box');
                                imgBox.dataset.index = i;
                                 switch (i) {
                                case 0:
                                    imgBox.style.gridColumn = "1 / 2";
                                    break;
                                case 1:
                                    imgBox.style.gridColumn = "2 / 3";
                                    break;
                                case 2:
                                    imgBox.style.gridColumn = "3 / 4";
                                    break;
                                case 3:
                                    imgBox.style.gridColumn = "1 / 2";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                                case 4:
                                    imgBox.style.gridColumn = "2 / 3";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                                case 5:
                                    imgBox.style.gridColumn = "3 / 4";
                                    imgBox.style.gridRow = " 2 / 3";
                                    break;
                            }
                                imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                                imgBox.style.backgroundSize = 'cover';
                                imgBox.style.backgroundPosition ='center center';
                                container.appendChild(imgBox);
                        }
                    break;
                        
                }
                    
                 // 이미지 박스 동적 생성 및 배치
                 /*for (let i = 0; i < imgNum; i++) {
                    const imgBox = document.createElement('div');
                    imgBox.classList.add('box');
                    imgBox.dataset.index = i;
                    imgBox.style.backgroundImage = `url('${imgArray[i]}')`;
                    imgBox.style.backgroundSize = 'cover';
                    container.appendChild(imgBox);
                  }*/
                
            } catch (error) {
                console.error('리뷰 목록을 불러오는 데 실패했습니다:', error);
            }
    }
     // End Function fillRestImg --------------------------------------------------
    

 });//end document