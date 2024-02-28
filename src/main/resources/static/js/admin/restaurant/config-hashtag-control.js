const hashtagSelect = document.querySelector("select#hashtag-select");
const hashtagCategory = {
    visitPurpose: "방문목적",
    mood: "분위기",
    convenience: "편의시설"
}
const categoryUpdateSelect = document.querySelector("select#tag-category-to-update")
let tagDataList = null;

const tbody = document.querySelector("tbody");

const btnUpdateSubmit = document.querySelector("button#btn-update-submit");

getHashtagCateogry(hashtagSelect);
getHashtagCateogry(categoryUpdateSelect);
getDataByCategory();

document.querySelector("input#tag-keyword").addEventListener("keyup", (e) => {
    resetTableBody();
    const tagFiltered = tagDataList.filter(el => el.keyword.includes(e.target.value.trim()));

    if (! tagFiltered.length) {
        document.querySelector("div#none-data-div").classList.remove("d-none");
    }else {
        document.querySelector("div#none-data-div").classList.add("d-none");
    }

    tagFiltered.forEach(mkTableRow);
})


async function getHashtagCateogry(hashtagSelect) {
    const {data: hashtagCategries} = await axios.get("./categories");

    hashtagCategries.forEach(el => {
        const option = document.createElement("option");

        option.innerHTML = hashtagCategory[el.name];
        option.value = el.id;

        hashtagSelect.appendChild(option);
    })
}

hashtagSelect.addEventListener("change", getDataByCategory)

async function getDataByCategory(e) {
    resetTableBody();
    document.querySelector("input#tag-keyword").value = "";

    const {data} = await axios.get(`../hashtag?categoryId=${e?.target?.value ?? ""}`);

    tagDataList = data;
    if (! data?.length) {
        document.querySelector("div#none-data-div").classList.remove("d-none");
    }else {
        document.querySelector("div#none-data-div").classList.add("d-none");
    }

    data.forEach(mkTableRow);

    addClickEventWhenRenderedList();
}

function mkTableRow(data) {
    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td><input data-id="${data.id}" class="select-tag" type="checkbox"/></td>
        <td class="category">${hashtagCategory[data.htCategory.name]}</td>
        <td>${data.keyword}</td>
        <td>${data.reviews.length}</td>
        <td><button class="btn-update form-control" data-id="${data.id}">수정</button></td>
        <td><button class="btn-delete form-control" data-id="${data.id}">삭제</button></td>
    `;

    tbody.appendChild(tr);
}

function resetTableBody() {
    tbody.innerHTML = "";
}

document.querySelector("button#btn-clear-all").addEventListener("click", () => {
        changeAllChecked(document.querySelectorAll("input[type='checkbox']"))
    }
)
const checkBoxOfTags = document.querySelectorAll("input.select-tag");

document.querySelector("input#check-all-tags").addEventListener("change", () => {
        changeAllChecked(checkBoxOfTags, document.querySelector("input#check-all-tags").checked)
    }
)

function changeAllChecked(checkBoxes, bool) {
    Array.from(checkBoxes).forEach(el => {
        el.checked = bool ?? false;
    })
}

function addClickEventWhenRenderedList() {
    const updateBtns = document.querySelectorAll("button.btn-update");
    updateBtns.forEach(el => el.addEventListener("click", getTagInfoToUpdate));
    const deleteBtns = document.querySelectorAll("button.btn-delete");
    deleteBtns.forEach(el => el.addEventListener("click", deleteTag))
}

async function getTagInfoToUpdate(ev) {
    document.querySelector("div#update-div-form").classList.remove("d-none");
    const {data} = await axios.get(`./${ev.target.getAttribute("data-id")}`);

    document.querySelector("input#tag-id-to-update").value = data.id;
    document.querySelector("input#tag-name-to-update").value = data.keyword;

    Array.from(categoryUpdateSelect.options).forEach(el => {
        el.selected = (parseInt(el.value) === data.htCategory.id);
    });
}

async function deleteTag(ev) {

    if (!confirm(`정말로 삭제하시겠습니까?
     ** 되돌릴 수 없습니다.
    `)) {
        return;
    }

    const dataId = ev.target.getAttribute("data-id");

    const {status} = await axios.delete(`../hashtag?tagId=${dataId}`);

    if (status === 204) {
        alert("태그가 삭제되었습니다.")
    }

    tbody.removeChild(ev.target.parentNode.parentNode);

    tagDataList = tagDataList.filter(e => parseInt(dataId) === e.id);

    if (! tagDataList.length) {
        document.querySelector("div#none-data-div").classList.remove("d-none");
    }else {
        document.querySelector("div#none-data-div").classList.add("d-none");
    }
}

btnUpdateSubmit.addEventListener("click", updateTagInfo)

async function updateTagInfo(ev) {
    const tagId = document.querySelector("input#tag-id-to-update").value.trim();
    const request = {
        tagName: document.querySelector("input#tag-name-to-update").value.trim(),
        categoryId: document.querySelector("select#tag-category-to-update").value.trim(),
    }
    console.log(request.tagId)
    const {status} = await axios.patch(`./${tagId}`, request);
    location.reload();
}

function getAllCheckedHashtags() {
    const tagsChecked = [];
    document.querySelectorAll("input.select-tag").forEach(el => {
        el.checked && tagsChecked.push(el.getAttribute("data-id"));
    });
    return tagsChecked;
}

document.querySelector("button#btn-remove-selected").addEventListener("click", async () => {
    if (!confirm(`정말로 삭제하시겠습니까?
     ** 되돌릴 수 없습니다.
    `)) {
        return;
    }

    const tagsChecked = getAllCheckedHashtags();

    console.log("tagsChecked")
    console.log(tagsChecked)

    const query = tagsChecked.map(el => `tagId=${el}`).join("&");
    const {status} = await axios.delete(`../hashtag?${query}`);
    location.reload();
})

document.querySelector("button#btn-remove-all").addEventListener("click", async () => {

    if (!confirm(`정말로 삭제하시겠습니까?
     ** 되돌릴 수 없습니다.
    `)) {
        return;
    }

    const dataIdList = Array.from(document.querySelectorAll("input.select-tag")).map(el => el.getAttribute("data-id"));
    const query = dataIdList.map(el => `tagId=${el}`).join("&");
    const {status} = await axios.delete(`../hashtag?${query}`);
    location.reload();
})

