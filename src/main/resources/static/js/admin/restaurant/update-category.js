document.addEventListener("DOMContentLoaded", configUpdatePage);
const categoryList = document.querySelector("ul#category-list");
let currentItemIndex = null;
let currentItem = null;

async function configUpdatePage() {
    const {data} = await axios.get(location.href + "/all");

    data.forEach(e => {
        const listItem = document.createElement("li");
        listItem.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-start");
        listItem.draggable = true;

        const hrefToRestaurantList = `./restaurant/all?categoryCond=${e.id}`;

        listItem.innerHTML = `
                        <input type="hidden" id="categoryId" value="${e.id}" />
                        <input type="hidden" id="categoryOrder" value="${e.order}" />
                        <div class="ms-2 me-auto">
                        <a href="${hrefToRestaurantList}" class="link-dark link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover"><div id="category-name-div" class="fw-bold fs-5">${e.name}</div></a>
                        </div>
                        <div>
                            <span class="badge bg-primary rounded-pill">${e.restaurantCnt}</span>
                            <button type="button" id="del-category-btn" class="btn  btn-outline-info">삭제</button>
                            <button type="button" id="update-category-btn" class="btn  btn-outline-info">수정</button>
                        </div>
                    `;
        categoryList.append(listItem);

        listItem.querySelector("button#del-category-btn").addEventListener("click", () => deleteCategoryHandler(listItem, e.name))
        listItem.querySelector("button#update-category-btn").addEventListener("click", () => updateCategoryHandler(listItem))
    })

}

async function deleteCategoryHandler(listItem, name) {
    if (!confirm(`카테고리 "${name}"을 삭제하시겠습니까?
                **관련 레스토랑
            `)) return;
    const categoryId = listItem.querySelector("input#categoryId").value;
    const {data} = await axios.delete(location.href + `/${categoryId}`);

    categoryList.removeChild(listItem);
}

async function updateCategoryHandler(listItem) {

    const updateCategoryContainer = categoryList.querySelector("div#update-category-container");

    console.log(updateCategoryContainer)

    if (updateCategoryContainer) {
        categoryList.removeChild(updateCategoryContainer.parentElement);
        return;
    }

    const li = document.createElement("li");
    li.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-start");

    li.innerHTML = `
                 <div id="update-category-container" class="input-group input-group-lg mb-3">
                          <input id="category-name" type="text" class="form-control is-valid" placeholder="변경할 이름" aria-label="category-name" aria-describedby="button-update-category-name" />
                          <button class="btn btn-outline-secondary" type="button" id="button-update-category-name">수정하기</button>
                 </div>
    `;

    listItem.after(li);
    const categoryId = listItem.querySelector("input#categoryId").value;
    const btnUpdateCategoryName = li.querySelector("button#button-update-category-name");
    const categoryNameToChange = li.querySelector("input#category-name");
    console.log(categoryNameToChange)
    btnUpdateCategoryName.addEventListener("click", async () => {
        if (categoryNameToChange.value && categoryNameToChange.value.trim().length < 2) {
            alert("유효한 이름을 입력해주세요.");
            return;
        }

        const {
            data,
            status
        } = await axios.patch(location.href + `/${categoryId}?categoryName=` + categoryNameToChange.value.trim());
        if (200 <= status && status < 300) {
            listItem.querySelector("#category-name-div").innerHTML = categoryNameToChange.value.trim();
            categoryList.removeChild(li);
        }
    })
}

// 드래그 드랍 가능 불가능

function removeAllDragEvent() {
    categoryList.removeEventListener("dragstart", dragStartHandler);
    categoryList.removeEventListener("dragover", dragOverHandler);
    categoryList.removeEventListener("drop", dropHandler);
}

function addDragAndDropEvent() {
    categoryList.addEventListener("dragstart", dragStartHandler);
    categoryList.addEventListener("dragover", dragOverHandler);
    categoryList.addEventListener("drop", dropHandler);
}

document.querySelectorAll('input[type=radio]').forEach(el => {
    el.addEventListener("change", () => {
        const canChange = document.querySelector('input[type=radio]:checked').value === "Y";
        if (canChange) {
            addDragAndDropEvent();
        } else {
            removeAllDragEvent();
        }
    })
});

// 드레그 앤 드랍
function dragOverHandler(e) {
    e.preventDefault();
}

function dragStartHandler(e) {
    currentItem = e.target;
    const childNodes = [...currentItem.parentElement.children];
    currentItemIndex = childNodes.indexOf(currentItem);
}

async function dropHandler(e) {
    const currentDropItem = e.target;
    let listArr = [...currentItem.parentElement.children];
    const dropItemIndex = listArr.indexOf(currentDropItem);

    if (currentItemIndex < dropItemIndex) {
        currentDropItem.after(currentItem);
    } else {
        currentDropItem.before(currentItem);
    }

    listArr = [...currentItem.parentElement.children];

    const categoryOrders = [];

    listArr.forEach(el => {
        const data = {};
        data.id = el.querySelector("input#categoryId").value;
        data.order = listArr.indexOf(el);
        categoryOrders.push(data);
    })

    const {data} = await axios.patch(location.href + "/order", categoryOrders);
}

const categoryNameToCreate = document.querySelector("input#category-name-to-create");
const addCategoryForm = document.getElementById("add-category-form");

document.getElementById("show-add-category-comp-btn").addEventListener("click", e => {
    addCategoryForm.classList.remove("d-none");
})

document.querySelector("button#cancel-add-category-btn").addEventListener("click", resetAddCategoryForm)

document.querySelector("button#add-category-btn").addEventListener("click", async () => {
    const categoryName = categoryNameToCreate.value;

    if (!categoryName && categoryName.trim().length < 1) {
        alert("유효한 카테고리 이름으로 작성해주세요.");
        return;
    }

    const {data} = await axios.post(location.href + "?categoryName=" + categoryName);
    console.log(data);

    const listItem = document.createElement("li");
    listItem.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-start");
    listItem.draggable = true;
    const hrefToRestaurantList = `./restaurant/all?categoryCond=${data.id}`;

    listItem.innerHTML = `
                        <input type="hidden" id="categoryId" value="${data.id}" />
                        <input type="hidden" id="categoryOrder" value="${data.listOrder}" />
                        <div class="ms-2 me-auto">
                            <a href="${hrefToRestaurantList}" class="link-dark link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover"><div id="category-name-div" class="fw-bold fs-5">${categoryName}</div></a>
                        </div>
                        <div>
                            <span class="badge bg-primary rounded-pill">${0}</span>
                            <button type="button" id="del-category-btn" class="btn  btn-outline-info">삭제</button>
                            <button type="button" id="update-category-btn" class="btn  btn-outline-info">수정</button>
                        </div>
                    `;

    categoryList.append(listItem);

    listItem.querySelector("button#del-category-btn").addEventListener("click", () => deleteCategoryHandler(listItem, categoryName));
    listItem.querySelector("button#update-category-btn").addEventListener("click", () => updateCategoryHandler(listItem));
    resetAddCategoryForm();
})

function resetAddCategoryForm() {
    categoryNameToCreate.value = "";
    addCategoryForm.classList.add("d-none");
}