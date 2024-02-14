const categorySelect = document.querySelector("select#category-option");
const statusSelect = document.querySelector("select#status-option");
const searchSelect = document.querySelector("select#search-option");
const searchKeyword = document.querySelector("input#search-keyword");
const keywordSearchBtn = document.querySelector("button#submit-btn");
const orderRadio = document.querySelectorAll("input[name='order']");

const data = {
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
        console.log(el);
        if (el.checked) {
            data.order = el.value;
        }
    });
}

categorySelect.addEventListener("change", () => {
    data.categoryCond = categorySelect.value;
    console.log(data);
    //    검색 axios 요청
    changeLocation();
})

statusSelect.addEventListener("change", () => {
    data.restaurantStatus = statusSelect.value;
    console.log(data);
    //    검색 axios 요청
    changeLocation();
})

keywordSearchBtn.addEventListener("click", () => {
    data.keywordCriteria = searchSelect.value;
    data.keyword = searchKeyword.value;
    console.log(data);
    //    검색 axios 요청
    changeLocation();
})

orderRadio.forEach(el => el.addEventListener("change", (e) => {
    data.order = e.target.value;
    console.log(data);
    changeLocation();
}));

async function submit() {
    let query = "";

    for (let key in data) {
        query += (key + "=");
        query += (data[key] + "&");
    }

    const {data: result} = await axios.get(`/admin/matzip/search?${query}`);
    console.log(result);
}

document.querySelectorAll("nav#pagination button").forEach(el =>
    el.addEventListener("click", (e) => {
        data.curPage = e.target.getAttribute("cur-page");
        changeLocation();
    })
)

function changeLocation() {
    location.href = `./all?${mkQueryString()}`;
}


function mkQueryString() {
    const queryParams = [];
    for (let key in data) {
        queryParams.push(`${key}=${data[key]}`);
    }

    return queryParams.join("&");
}


