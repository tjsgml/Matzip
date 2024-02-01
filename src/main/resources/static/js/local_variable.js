async function searchByKeyword() {
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
    removeAllMarkers();
    keywordInput.value = "";
    addrInput.value = "";

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // const keyword = "제주특별자치도 제주시 첨단로 242";

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