const searchParams = new URLSearchParams(location.urlSearchParams);

const reqList = document.querySelector("tbody#request-list-table");
const pagination = document.querySelector("ul.pagination");
const checkboxAll = document.querySelector("input#check-all-req");
const updateBtnInModal = document.querySelector("button#btn-update-in-modal");

let infoUpdatedListItem = null;

const query = {
    status: searchParams.get("status") ?? null,
    keyword: searchParams.get("keyword") ?? null,
    page: searchParams.get("page") ?? null
};

const STATUS_ENUM = {
    WAITING: "대기",
    APPROVED: "완료"
}

function resetRequestList() {
    reqList.innerHTML = "";
}

renderRequestList();

async function renderRequestList() {
    const nothingDiv = document.querySelector("div#nothing-div");
    checkboxAll.checked = false;
    resetRequestList();

    let {data, status} = await axios.get("./requests?" + mkQueryString(query));

    if (!data || !data.length) {
        nothingDiv.classList.add("d-none");
    } else {
        nothingDiv.classList.remove("d-none");
    }

    const {totalPages, number, content: listItems} = data;
    listItems?.forEach(mkTableRow);

    reqList.querySelectorAll("button.update-status-btn").forEach(el => {
        el.addEventListener("click", completeStatus);
    })

    renderPagination(pagination, totalPages, number, renderRequestList);
}

function mkTableRow(el) {
    const tr = document.createElement("tr");
    tr.classList.add("text-center");

    tr.innerHTML = `  
        <td>
            <input class="check-req" data-id="${el.id}" type="checkbox" ${el.status === "APPROVED" ? "disabled" : ""}/>
        </td>
        <td><a class="nav-link" href="./restaurant/${el.restaurant.id}">${el.restaurant.name}</a></td>
        <td>
            <button type="button" class="container bg-white border border-0 hover-secondary" 
            style="text-overflow: ellipsis;white-space: nowrap;overflow: hidden" data-bs-toggle="modal" data-id="${el.id}" 
            data-bs-target="#requestToUpdateModal">
                ${el.content}
            </button>
        </td>
        <td>
        ${el.status === "WAITING" ? '<span id="status-badge" class="ms-2 badge rounded-pill text-bg-danger">대기</span>' :
        '<span id="status-badge" class="ms-2 badge rounded-pill text-bg-success">완료</span>'}
        </td>
        <td>
            <button class="btn btn-sm btn-primary update-status-btn" data-id=${el.id} ${el.status === "WAITING" ? "" : "disabled"}>
                처리
            </button>
        </td>
    `;

    reqList.appendChild(tr);
}

const keywordInput = document.querySelector("input#search-keyword");

document.querySelector("select#search-option").addEventListener("change", () => {
    query.status = document.querySelector("select#search-option").value;
    query.page = 0;
    renderRequestList();
})

document.querySelector("button#submit-btn").addEventListener("click", () => {
    if (keywordInput.value < 1) {
        alert("검색어를 입력해주세요.");
        return;
    }

    query.keyword = keywordInput.value;
    query.page = 0;
    renderRequestList();
})

checkboxAll.addEventListener("change", () => {
    document.querySelectorAll("input.check-req").forEach(el => {
        el.checked = el.disabled ? false : checkboxAll.checked;
    })
})

const requestToUpdateModal = document.getElementById('requestToUpdateModal')
if (requestToUpdateModal) {
    requestToUpdateModal.addEventListener('show.bs.modal', async (event) => {
        const button = event.relatedTarget;
        infoUpdatedListItem = button.parentNode.parentNode;

        const reqId = button.getAttribute('data-id')

        const {data, status} = await axios.get(`./requests/${reqId}`);
        if (status !== 200) {
            return;
        }

        const restaurantName = requestToUpdateModal.querySelector('span#restaurant-name-in-modal');
        const statusBadge = requestToUpdateModal.querySelector('span#status-badge');
        const reqContent = requestToUpdateModal.querySelector('span#req-content');

        restaurantName.innerHTML = data.restaurant.name;
        statusBadge.innerHTML = STATUS_ENUM[data.status];
        updateBtnInModal.setAttribute("data-id", reqId);

        if (data.status === "WAITING") {
            statusBadge.classList.add("text-bg-danger");
            statusBadge.classList.remove("text-bg-success");
            updateBtnInModal.disabled = false;
        } else {
            statusBadge.classList.add("text-bg-success");
            statusBadge.classList.remove("text-bg-danger");
            updateBtnInModal.disabled = true;
        }

        reqContent.innerHTML = data.content;

    })
}

updateBtnInModal.addEventListener("click", (e) => completeStatus(e, updateBtnInModal));

async function completeStatus(e, updateBtnInModal) {

    if (!confirm("해당 요청을 완료처리하시겠습니까?")) {
        return;
    }

    const updateTarget = updateBtnInModal ?? e.target;
    const reqId = updateTarget.getAttribute("data-id");
    const {data} = await axios.patch(`./requests?reqId=${reqId}`);

    if (!updateBtnInModal) {
        infoUpdatedListItem = updateTarget.parentNode.parentNode;
    }

    changeNewStatus(infoUpdatedListItem);
}

document.querySelector("button#complete-multiple-req-btn").addEventListener("click", async () => {
    const listItemsToUpdate = [];
    const parents = [];

    document.querySelectorAll("input.check-req").forEach(el => {
        if (el.checked) {
            listItemsToUpdate.push(`reqId=${el.getAttribute("data-id")}`);
            parents.push(el.parentNode.parentNode);
        }
    })

    const {status} = await axios.patch(`./requests?${listItemsToUpdate.join("&")}`);

    if (status === 200) {
        checkboxAll.checked = false;
        parents.forEach(changeNewStatus);
    }
})

/**
 * @param infoUpdatedListItem tr 엘리멘트를 넘기면 됨
 */
function changeNewStatus(infoUpdatedListItem) {
    const statusBadge = infoUpdatedListItem.querySelector("span#status-badge");
    const checkBox = infoUpdatedListItem.querySelector("input.check-req");
    const updateButton = infoUpdatedListItem.querySelector("button.update-status-btn");

    statusBadge.classList.add("text-bg-success");
    statusBadge.classList.remove("text-bg-danger");

    statusBadge.innerHTML = "완료";
    checkBox.checked = false;
    checkBox.disabled = true;
    updateButton.disabled = true;
}