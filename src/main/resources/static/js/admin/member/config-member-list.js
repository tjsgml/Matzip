const memberListTable = document.querySelector("tbody#member-list-table");
const searchOptionSelect = document.querySelector("select#search-option");
const searchKeyword = document.querySelector("input#search-keyword");
const searchKeywordBtn = document.querySelector("button#submit-btn");
const viewCntRadio = document.querySelectorAll("input[name='viewCnt']");
const authorizeOption = document.querySelector("select#authorize-option");
const paginationList = pagination.querySelector("#pagination>ul.pagination");

const memberListNav = document.getElementById("member-list");

const nothingDiv = document.createElement("div");
nothingDiv.classList.add("container", "card", "mt-4", "border-0");
nothingDiv.style = 'style="border-radius: 24px; box-shadow: 0px 0px 10px 0px rgb(230, 230, 230);"';
nothingDiv.innerHTML = `<div class="card-body pt-5">
                    <div class="row fw-semibold text-center"
                         style="font-size: 20px;">
                        <div class="col my-5">
                            <p style="font-size: 50px">😂👀😳😦</p>
                            <div>
                                해당 검색 결과에 알맞은 유저가 없습니다.
                            </div>
                        </div>
                    </div>
                </div>`;

const query = {
    role: "",
    searchCondition: "",
    searchKeyword: "",
    viewCnt: "10",
    page: "0"
}

viewCntRadio.forEach(el => {
    console.log(el)
    el.addEventListener("change", (e) => {
        query.viewCnt = e.target.value;
        query.page = 0;
        resetSearchBox();
        renderMemberList();
    })
})

authorizeOption.addEventListener("change", e => {
    query.role = e.target.value;
    query.page = 0;
    resetSearchBox();
    renderMemberList();
})

renderMemberList();

function resetSearchBox() {
    query.searchCondition = "";
    query.searchKeyword = "";
    searchOptionSelect.querySelector('option[name="default"]').selected = true;
    searchKeyword.value = "";
}

function resetMemberList() {
    memberListTable.innerHTML = "";
}

async function renderMemberList() {
    resetMemberList();
    console.log(query);
    let {data} = await axios.get(location.href + "/list?" + mkRequestQuery());
    const {totalPages, number, content: listItems} = data;
    renderPagination(totalPages, number);


    if (!listItems || listItems.length === 0) {
        memberListNav.appendChild(nothingDiv);
        paginationList.classList.add("d-none");
    } else {
        if (memberListNav.contains(nothingDiv)) memberListNav.removeChild(nothingDiv);
        paginationList.classList.remove("d-none");
    }

    listItems.forEach(listItem => {
        const tableRow = document.createElement("tr");
        tableRow.innerHTML = `<tr>
                   <td><a href="./member/${listItem.id}">${listItem.id}</a></td>
                   <td><a href="./member/${listItem.id}">${listItem.username}</a></td>
                   <td><a href="./member/${listItem.id}">${listItem.nickname}</a></td>
                   <td>${listItem.email}</td>
                   <td>${listItem.roles.join(", ")}</td>
                   <td>
                       <a class="btn btn-outline-success p-1" href="./member/${listItem.id}?review=Y">회원 리뷰</a>
                   </td>
                   <td>
                       <button class="btn-del btn btn-outline-secondary p-1">사용자 삭제
                       </button>
                   </td>
               </tr>`;

        memberListTable.append(tableRow);

        tableRow.querySelector("button.btn-del").addEventListener("click", async () => {
            const {status} = axios.delete(location.href + "/" + listItem.id);
            console.log(status);
        })
    });

}

function renderPagination(totalPages, curPage) {
    paginationList.innerHTML = "";
    mkPaginationListItem(0, curPage, "≪");

    for (let i = 0; i < totalPages; i++) {
        mkPaginationListItem(i, curPage, i + 1);
    }
    mkPaginationListItem(totalPages - 1, curPage, "≫");
}

function mkPaginationListItem(num, curPage, txt) {
    const paginationListItem = document.createElement("li");
    paginationListItem.classList.add("page-item");
    if (curPage === num) paginationListItem.classList.add("disabled");

    paginationListItem.innerHTML = `
    <button class="page-link" ${curPage === num ? "disabled" : ""}>${txt}</button>
    `;

    paginationList.appendChild(paginationListItem);

    paginationListItem.querySelector("button.page-link").addEventListener("click", () => {
        query.page = num;
        renderMemberList();
    })
}

function mkRequestQuery() {

    const part = [];
    const key = Object.keys(query);

    key.forEach(k => {
        part.push(`${k}=${query[k]}`);
    })

    return part.join("&");
}

searchKeywordBtn.addEventListener("click", () => {
    if (searchKeyword.value && searchKeyword.value.length > 0) {
        query.searchCondition = searchOptionSelect.value;
        query.searchKeyword = searchKeyword.value;

        renderMemberList();
        renderPagination();
        return;
    }
    alert("검색어를 입력해주세요.")
})