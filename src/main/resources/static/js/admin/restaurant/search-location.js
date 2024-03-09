async function searchByKeyword() {
    console.log("키워드검색입니다.")
    removeAllMarkers();
    keywordInput.value = "";
    addrInput.value = "";

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }
    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다

    const radius = selectDistance.options[selectDistance.options.selectedIndex].value;
    const query = `query=${keyword}&y=${origin_lat}&x=${origin_lng}&radius=${radius || 5000}&sort=distance&size=${reqSize}&page=${curPage}`;
    const resp = await axios.get(`${SEARCH_BY_KEYWORD_URL}${query}`, {
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            Authorization: `KakaoAK ${REST_API_KEY}`
        }
    })

    const {documents, meta} = resp.data;

    pagination(meta.pageable_count);

    if (!documents.length) {
        alert("검색 결과가 없습니다.");
        return;
    }

    setCenter(documents[0].y, documents[0].x);
    documents.forEach(el => setMarker(el));
}

async function searchByAddress() {
    console.log("주소검색입니다.")
    removeAllMarkers();
    keywordInput.value = "";
    addrInput.value = "";

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    const query = `query=${keyword}&size=10&analyze_type=similar&page=${curPage}&size=${reqSize}`;
    const url = `${SEARCH_BY_ADDRESS_URL}${query}`;
    const resp = await axios.get(url + query, {
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            Authorization: `KakaoAK ${REST_API_KEY}`
        }
    });

    const {documents, meta} = resp.data;
    pagination(meta.pageable_count);

    if (!documents.length) {
        alert("검색 결과가 없습니다.");
        return;
    }

    documents.forEach(el => el.id = Math.floor(Math.random() * 10000000000))
    documents.forEach(el => setMarker(el));
    console.dir(documents)
    const {x, y} = documents[0];
    const markerPosition = new kakao.maps.LatLng(y, x);
    map.setCenter(markerPosition);
}

function removeAllMarkers() {
    markers.forEach(el => el.setMap(null));
    markers = [];
    document.getElementById("placesList").innerHTML = "";
    paginationComp.innerHTML = "";
}

function getCurrentCord () {
    origin_lng = map.getCenter().getLng();
    origin_lat = map.getCenter().getLat();
}


function pagination(total_num) {
    paginationComp.innerHTML = "";

    totalPage = Math.ceil(total_num / (reqSize * 1.0));
    curPage = curPage || 1;

    for (let i = 1; i <= totalPage; i++) {
        const radio = document.createElement("input");
        radio.classList.add("btn-check");
        radio.name = "pageOption";
        radio.id = "option" + i;
        radio.autocomplete = false;
        radio.value = i;

        const labelForRadio = document.createElement("label");
        labelForRadio.classList.add("btn");
        labelForRadio.htmlFor = "option" + i;
        labelForRadio.innerHTML = i;

        if (i === curPage) {
            radio.classList.add("selected");
            radio.defaultChecked = true;
        }

        radio.addEventListener("click", () => {
            switch (searchType) {
                case "KEYWORD" :
                    curPage = radio.value;
                    searchByKeyword();
                    break;
                case "ADDR" :
                    curPage = radio.value;
                    searchByAddress();
                    break;
            }
        })

        paginationComp.append(radio);
        paginationComp.append(labelForRadio);
    }

}

function setMarker(el) {

    const markerPosition = new kakao.maps.LatLng(el.y, el.x);
    const marker = new kakao.maps.Marker({
        position: markerPosition,
        map: map
    });

    markers.push(marker);

    const infowindowContent = document.createElement("div");

    infowindowContent.classList.add("overlay_info");
    infowindowContent.style.zIndex = 0;

    infowindowContent.innerHTML = `
                <div>
                  <a href="${el.place_url || ''}" target="_blank"><strong>${el.place_name || ""}</strong></a>
                </div>
                   <div class="desc">
                   <div>
                      <span>주소 : </span>
                      <span>${el.address_name || ""}</span>
                   </div>
                   <div>
                      <span>전화 번호 : </span>
                      <span>${el.phone ?? ""}</span>
                   </div>
                 </div>
                <input id="placename-Input-${el.id}" type="hidden" value="${el.place_name || ''}" />
                <input id="address-Input-${el.id}" type="hidden" value="${el.address_name || ''}" />
                <input id="phone-Input-${el.id}" type="hidden" value="${el.phone || ''}" />
                <input id="lat-Input-${el.id}" type="hidden" value="${el.y}" />
                <input id="lng-Input-${el.id}" type="hidden" value="${el.x}" />
       `;

    console.log(`lng-Input-${el.id}`)

    // iwPosition = new kakao.maps.LatLng(33.450701, 126.570667); //인포윈도우 표시 위치입니다

// 인포윈도우를 생성합니다
    const infowindow = new kakao.maps.InfoWindow({
        position: markerPosition,
        content: infowindowContent,
    });

    kakao.maps.event.addListener(marker, "mouseover", (event) => {
        marker.setZIndex(10);
        infowindow.open(map, marker);
        // setCenter(el.x, el.y);
    })

    kakao.maps.event.addListener(marker, "mouseout", (event) => {
        marker.setZIndex(0);
        infowindow.close();
    })
    kakao.maps.event.addListener(marker, "click", () => setAddressInfo(el.id, infowindow));
    setList(el, marker, infowindow);
}


function setList(el, marker, infowindow) {
    const listItem = document.createElement("li");
    listItem.style.listStyle = "none";
    listItem.classList.add("card");

    listItem.innerHTML = `
            <div class="card-body">
                  <a href="${el.place_url}">
                    <h5 class="card-title">${el.place_name || el.address_name}</h5>
                   <span class="fs-6"> ${el.category_name ? " | " + el.category_name : ""}</span>
                   </a>
                  <div class="d-flex align-items-center">
                    <div class="card-icon rounded-circle d-flex align-items-center justify-content-center">
                       전화번호 : <span>${el.phone || " 등록된 전화번호가 없습니다."}</span>
                    </div>
                    <button id="select-${el.id}">선택</button>
                  </div>
                </div>
        `;


    const onmouseover = () => {
        listItem.classList.add("active");
        marker.setZIndex(10);
        infowindow.open(map, marker);
    }

    const onmouseout = () => {
        listItem.classList.remove("active");
        marker.setZIndex(0);
        infowindow.close();
    }

    listItem.addEventListener("mouseover", onmouseover)
    listItem.addEventListener("mouseout", onmouseout)
    document.getElementById("placesList").append(listItem);

    const selectBtn = document.getElementById("select-" + el.id);
    selectBtn.addEventListener("click", () => {
        setAddressInfo(el.id, infowindow);
    });
}
