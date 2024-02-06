let markers = [];
<!--  맵 관련 초기 설정  -->
const mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.514322572335935, 127.06283102249932), // 지도의 중심좌표
        draggable: true,
        level: 3 // 지도의 확대 레벨
    };

const map = new kakao.maps.Map(mapContainer, mapOption, {useMapBounds: true}); // 지도를 생성합니다

const draggableMarker = new kakao.maps.Marker(
    {
        position: map.getCenter(),
        map,
        draggable: true
    }
);

changeDraggableMarkerPosition(draggableMarker.getPosition());

// 센터 정하는 메서드
function setCenter(lat, lng) {
    const moveLatLon = new kakao.maps.LatLng(lat, lng);
    map.setCenter(moveLatLon);
}

kakao.maps.event.addListener(map, 'center_changed', function () {
    draggableMarker.setPosition(map.getCenter());
    changeDraggableMarkerPosition(map.getCenter());
});

kakao.maps.event.addListener(draggableMarker, 'dragend', function () {
    getCurrentCord();
    changeDraggableMarkerPosition(draggableMarker.getPosition());
});

async function changeDraggableMarkerPosition(latLng) {
    setCenter(latLng.getLat(), latLng.getLng());
    const query = `y=${latLng.getLat()}&x=${latLng.getLng()}&page=${curPage}&size=${reqSize}`;
    const resp = await axios.get(`https://dapi.kakao.com/v2/local/geo/coord2address.json?${query}`, {
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            Authorization: `KakaoAK ${REST_API_KEY}`
        }
    });

    // console.log(resp)
    const address = await resp.data.documents[0]?.address;

    const infowindowContent = document.createElement("div");

    infowindowContent.classList.add("overlay_info");
    infowindowContent.style.zIndex = 0;

    infowindowContent.innerHTML = `
                 <div class="desc">
                   <div>
                      <span>주소 : </span>
                      <span>${address?.address_name ?? "등록된 주소가 없습니다."}</span>
                   </div>
                   <div>
                      <span>전화 번호 : </span>
                      <span>등록된 전화번호가 없습니다.</span>
                   </div>
                 </div>
                <input id="placename-Input-center" type="hidden" value="${address?.place_name || ''}" />
                <input id="address-Input-center" type="hidden" value="${address?.address_name || ''}" />
                <input id="phone-Input-center" type="hidden" value="${address?.phone || ''}" />
                <input id="lat-Input-center" type="hidden" value="${latLng.getLat()}" />
                <input id="lng-Input-center" type="hidden" value="${latLng.getLng()}" />
       `;

    // iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다

// 인포윈도우를 생성합니다
    const infowindow = new kakao.maps.InfoWindow({
        position: draggableMarker.getPosition(),
        content: infowindowContent,
    });

// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
    kakao.maps.event.addListener(draggableMarker, "mouseover", (event) => {
        draggableMarker.setZIndex(10);
        infowindow.open(map, draggableMarker);
    })

    kakao.maps.event.addListener(draggableMarker, "mouseout", (event) => {
        draggableMarker.setZIndex(0);
        infowindow.close();
    })

    kakao.maps.event.addListener(draggableMarker, "click", () => setAddressInfo("center", infowindow));
}

// 지도, 스카이뷰 선택
const mapTypeControl = new kakao.maps.MapTypeControl();
map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

// 지도 드래그 가능 여부 설정
const draggableBtn = document.getElementById("draggable-btn")
draggableBtn.addEventListener("click", () => {
    map.setDraggable(true);
    draggableBtn.classList.add("active");
    unDraggableBtn.classList.remove("active");
});

const unDraggableBtn = document.getElementById("un-draggable-btn")
unDraggableBtn.addEventListener("click", (ev) => {
    map.setDraggable(false);
    draggableBtn.classList.remove("active");
    unDraggableBtn.classList.add("active");
});


keywordSearchBtn.addEventListener("click", () => {
    curPage = 1;
    searchType = "KEYWORD";
    keyword = keywordInput.value;
    getCurrentCord();
    searchByKeyword();
});

// 키워드 검색

