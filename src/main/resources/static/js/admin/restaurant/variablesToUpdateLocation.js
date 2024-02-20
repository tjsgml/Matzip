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

const restaurantIdInput = document.getElementById("restaurantIdInput");
const nameInput = document.getElementById("nameInput");
const addrInput = document.getElementById("addrInput");
const detailAddrInput = document.getElementById("detailAddrInput");
const contactInput = document.getElementById("contactInput");
const lngInput = document.getElementById("lngInput");
const latInput = document.getElementById("latInput");
const categorySelect = document.querySelector("select#select-category");
const statusSelect = document.querySelector("select#select-status");

const originRestaurantInfo = {
    name: nameInput.value,
    addr: addrInput.value,
    detailAddr: detailAddrInput.value,
    lat: latInput.value,
    lng: lngInput.value,
    contact: contactInput.value,
    category: categorySelect.value
}

const paginationComp = document.getElementById("pagination");
const selectDistance = document.getElementById("selectDistance");

const REST_API_KEY = "c89a020dd130ee8b16d5aba0dc567c8e";
const SEARCH_BY_KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword.JSON?";
const SEARCH_BY_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.JSON?";

let origin_lat = latInput.value ?? 37.514322572335935;
let origin_lng = lngInput.value ?? 127.06283102249932;

function resetAddress() {
    nameInput.value = originRestaurantInfo.name;
    addrInput.value = originRestaurantInfo.addr;
    detailAddrInput.value = originRestaurantInfo.detailAddr;
    latInput.value = originRestaurantInfo.lat;
    lngInput.value = originRestaurantInfo.lng;
    contactInput.value = originRestaurantInfo.contact;
    categorySelect.value = originRestaurantInfo.category;
}

function setAddressInfo(locId, infoWindow) {
    detailAddrInput.value = "";
    const address = document.getElementById(`address-Input-${locId}`).value;
    const lng = document.getElementById(`lng-Input-${locId}`).value;
    const lat = document.getElementById(`lat-Input-${locId}`).value;

    addrInput.value = address;
    lngInput.value = lng;
    latInput.value = lat;
    document.getElementById("placesList").innerHTML = "";
    removeAllMarkers();
    infoWindow.close();
}

document.querySelector("button#reset-button").addEventListener("click", resetAddress)