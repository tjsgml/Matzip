const type = "CREATE";

let curPage = 1;
let totalPage = 0;
let reqSize = 10;
let keyword = "";
let searchType = "";

const keywordInput = document.getElementById("keywordInput");
const keywordSearchBtn = document.getElementById("keywordSearchBtn")
const addrSearchInput = document.getElementById("addrSearchInput");
const addrSearchBtn = document.getElementById("addrSearchBtn");

addrSearchBtn.addEventListener("click", () => {
    curPage = 1;
    keyword = addrSearchInput.value;
    searchType = "ADDR";
    searchByAddress();
});

const nameInput = document.getElementById("nameInput");
const addrInput = document.getElementById("addrInput");
const detailAddrInput = document.getElementById("detailAddrInput");
const contactInput = document.getElementById("contactInput");
const lngInput = document.getElementById("lngInput");
const latInput = document.getElementById("latInput");
const categorySelect = document.querySelector("select#select-category");

const paginationComp = document.getElementById("pagination");
const selectDistance = document.getElementById("selectDistance");

const REST_API_KEY = "c89a020dd130ee8b16d5aba0dc567c8e";
const SEARCH_BY_KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword.JSON?";
const SEARCH_BY_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.JSON?";

let origin_lat = 37.49807646847611;
let origin_lng = 127.02781933297571;

function resetAddress() {
    nameInput.value = "";
    addrInput.value = "";
    contactInput.value = "";
    lngInput.value = "";
    latInput.value = "";
    detailAddrInput.value = "";
}

function setAddressInfo(locId, infoWindow) {
    resetAddress();
    const placeName = document.getElementById(`placename-Input-${locId}`).value;
    const address = document.getElementById(`address-Input-${locId}`).value;
    const phone = document.getElementById(`phone-Input-${locId}`).value;
    const lat = document.getElementById(`lat-Input-${locId}`).value;
    const lng = document.getElementById(`lng-Input-${locId}`).value;

    nameInput.value = placeName;
    addrInput.value = address;
    contactInput.value = phone;

    lngInput.value = lng;
    latInput.value = lat;
    document.getElementById("placesList").innerHTML = "";
    removeAllMarkers();
    infoWindow.close();
}