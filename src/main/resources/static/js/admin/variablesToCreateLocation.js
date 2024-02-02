
let curPage = 1;
let totalPage = 0;
let reqSize = 10;
let keyword = "";
let searchType = "";
let origin_lat = "";
let origin_lng = "";

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

const paginationComp = document.getElementById("pagination");
const selectDistance = document.getElementById("selectDistance");

const REST_API_KEY = "c89a020dd130ee8b16d5aba0dc567c8e";
const SEARCH_BY_KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword.JSON?";
const SEARCH_BY_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.JSON?";
