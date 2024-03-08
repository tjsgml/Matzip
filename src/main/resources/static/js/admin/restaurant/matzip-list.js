const resource = "restaurant";

const categorySelect = document.querySelector("select#category-option");
const statusSelect = document.querySelector("select#status-option");
const searchSelect = document.querySelector("select#search-option");
const searchKeyword = document.querySelector("input#search-keyword");
const keywordSearchBtn = document.querySelector("button#submit-btn");
const orderRadio = document.querySelectorAll("input[name='order']");

const query = {
    categoryCond: categorySelect.value,
    restaurantStatus: statusSelect.value,
    keywordCriteria: searchSelect.value,
    keyword: searchKeyword.value,
    order: null,
    curPage: 0,
    elementPerPage: 10 // fixed
}

getCheckedEl();

function getCheckedEl() {
    orderRadio.forEach(el => {
        if (el.checked) {
            query.order = el.value;
        }
    });
}

categorySelect.addEventListener("change", () => {
    query.categoryCond = categorySelect.value;
    //    검색 axios 요청
    changeLocation();
})

statusSelect.addEventListener("change", () => {
    query.restaurantStatus = statusSelect.value;
    //    검색 axios 요청
    changeLocation();
})

keywordSearchBtn.addEventListener("click", () => {
    query.keywordCriteria = searchSelect.value;
    query.keyword = searchKeyword.value;
    //    검색 axios 요청
    changeLocation();
})

orderRadio.forEach(el => el.addEventListener("change", (e) => {
    query.order = e.target.value;
    changeLocation();
}));

document.querySelectorAll("nav#pagination button").forEach(el =>
    el.addEventListener("click", (e) => {
        query.curPage = e.target.getAttribute("cur-page");
        changeLocation();
    })
)

function changeLocation() {
    location.href = `./all?${mkQueryString(query)}`;
}


function mkQueryString(query) {
    const queryParams = [];
    for (let key in query) {
        queryParams.push(`${key}=${query[key]}`);
    }

    return queryParams.join("&");
}


