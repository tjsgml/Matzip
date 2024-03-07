const btnUpdateMenuPrices = document.querySelectorAll("button.btn-update-menu-price");
const btnUpdateMenuNames = document.querySelectorAll("button.btn-update-menu-name");
const itemNoneFoundDiv = document.querySelector("div#item-none-found");
const restaurantId = document.getElementById("restaurant-id").value;

btnUpdateMenuPrices.forEach(el => {
    el.addEventListener("click", showCompForUpdatePrice);

    const updateMenuPriceComp = el.parentElement.querySelector("#update-menu-price-comp");
    const updatePriceBtn = updateMenuPriceComp.querySelector("button#updatePriceBtn");

    updatePriceBtn.addEventListener("click", updatePrice);

})

async function updatePrice(e) {
    const updateMenuPriceComp = e.target.parentElement.parentElement.querySelector("#update-menu-price-comp");
    const menuId = updateMenuPriceComp.getAttribute("data-id");
    const priceToUpdate = updateMenuPriceComp.querySelector("input#priceToUpdate");

    if (! /^\d+$/.test(String(priceToUpdate.value))) {
        alert("가격은 정수로만 입력해주세요.");
        priceToUpdate.value = null;
        return;
    }

    const checkBeforeUpdate = confirm("정말 변경하시겠습니까?");
    if (!checkBeforeUpdate) {
        priceToUpdate.value = null;
        return;
    }


    const {data, status} = await axios.patch(`./menu/${menuId}/price?price=${priceToUpdate.value}`);

    if (status === 200) {
        const priceComp = document.querySelector("span#price-" + menuId);
        priceComp.innerHTML = priceToUpdate.value;
        priceToUpdate.value = null;
        updateMenuPriceComp.classList.add("d-none");
    } else {
        alert("가격 변경에 실패했습니다! 다시 시도해주세요.")
    }
}

function showCompForUpdatePrice(e) {
    const updateMenuPriceComp = e.target.parentElement.querySelector("#update-menu-price-comp");
    if (updateMenuPriceComp.classList.contains("d-none")) {
        updateMenuPriceComp.classList.remove("d-none");
    } else {
        updateMenuPriceComp.classList.add("d-none");
    }
}

btnUpdateMenuNames.forEach(el => {
    el.addEventListener("click", showCompForUpdateName);

    const updateMenuNameComp = el.parentElement.querySelector("#update-menu-name-comp");
    const updateNameBtn = updateMenuNameComp.querySelector("button#updateNameBtn");

    updateNameBtn.addEventListener("click", updateName);
})

async function updateName(e) {
    const updateMenuNameComp = e.target.parentElement.parentElement.querySelector("#update-menu-name-comp");
    console.log(updateMenuNameComp.getAttribute("data-id"))
    const menuId = updateMenuNameComp.getAttribute("data-id");
    const nameToUpdate = updateMenuNameComp.querySelector("input#nameToUpdate");

    if (!confirm("정말 변경하시겠습니까?")) {
        nameToUpdate.value = null;
        return;
    }

    if (nameToUpdate.value.trim().length <= 0) {
        alert("수정할 메뉴 이름을 입력해주세요.");
    }

    const {data, status} = await axios.patch(`./menu/${menuId}/name?name=${nameToUpdate.value}`);

    if (status === 200) {
        const nameComp = document.querySelector("span#name-" + menuId);
        nameComp.innerHTML = nameToUpdate.value;
        nameToUpdate.value = null;
        updateMenuNameComp.classList.add("d-none");
    } else {
        alert("이름변경에 실패했습니다! 다시 시도해주세요.")
    }
}

function showCompForUpdateName(e) {
    const updateMenuNameComp = e.target.parentElement.querySelector("#update-menu-name-comp");
    if (updateMenuNameComp.classList.contains("d-none")) {
        updateMenuNameComp.classList.remove("d-none");
    } else {
        updateMenuNameComp.classList.add("d-none");
    }
}

document.querySelectorAll("button.btn-del").forEach(el => el.addEventListener("click", deleteMenu))

async function deleteMenu(e) {

    if (!confirm(`정말로 삭제하시겠습니까?
        *되돌릴 수 없습니다.`)) {
        return;
    }

    const {status} = await axios.delete(`./menu?menuId=${e.target.getAttribute("data-id")}`);

    if (status !== 204) {
        alert("삭제과정에 문제가 발생하였습니다.")
        return;
    }

    if (itemNoneFoundDiv.classList.contains("d-none")) {
        itemNoneFoundDiv.classList.remove("d-none");
    }

    const listItem = e.target.parentElement.parentElement;
    document.getElementById("menuList").removeChild(listItem);

}

const addMenuBtn = document.getElementById("add-menu-btn");
const menuList = document.getElementById("menuList");
const createMenu = document.getElementById("create-menu-input-group");

addMenuBtn.addEventListener("click", e => {
    if (createMenu.classList.contains("d-none")) {
        createMenu.classList.remove("d-none");
        return;
    }
    createMenu.classList.add("d-none");
})

const createMenuBtn = document.getElementById("create-menu-btn");

createMenuBtn.addEventListener("click", async () => {
    const menuNameInput = document.getElementById("menu-name-to-create");
    const menuPriceInput = document.getElementById("menu-price-to-create");

    if (menuNameInput.value.trim().length <= 0 || menuPriceInput.value.trim().length <= 0) {
        alert("메뉴 이름과 가격을 입력해주세요!");
        return;
    }

    console.log("들어오냐구,,,,")

    const {data, status} = await axios.post(`./${restaurantId}/menu/one`, {
        name: menuNameInput.value,
        price:
        menuPriceInput.value
    });

    if (!(200 <= status < 300)) {
        alert("메뉴 등록에 실패했습니다.")
        return;
    }

    const listItemToAdd = document.createElement("li");
    listItemToAdd.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");

    listItemToAdd.innerHTML = `
        <div>
                <span id="name-${data}">${menuNameInput.value}</span>
                <button class="btn btn-sm btn-update-menu-name">이름 변경</button>
                <div class="input-group d-none" id="update-menu-name-comp" data-id=${data}>
                    <input id="nameToUpdate" class="form-control form-control-sm" type="text"
                        placeholder="변경할 이름을 입력해주세요."/>
                    <button id="updateNameBtn" class="btn btn-sm btn-outline-primary">확인</button>
                </div>
        </div>
        <div class="d-flex justify-content-between">
            <div class="text-end">
                <span id='price-${data}'  class="badge bg-primary rounded-pill"
                >${menuPriceInput.value}</span>
                <button class="btn btn-sm btn-update-menu-price">가격 변경</button>
                <div class="input-group d-none" id="update-menu-price-comp" data-id=${data}>
                        <input id="priceToUpdate" class="form-control form-control-sm" type="text"
                        placeholder="변경할 가격 입력"/>
                        <button id="updatePriceBtn" class="btn btn-sm btn-outline-primary">확인</button>
                </div>
            </div>
            <button class="btn btn-sm btn-del btn" data-id=${data}>X</button>
        </div>
    `;

    menuList.appendChild(listItemToAdd);

    listItemToAdd.querySelector("button.btn-update-menu-name").addEventListener("click", showCompForUpdateName);
    listItemToAdd.querySelector("button.btn-update-menu-price").addEventListener("click", showCompForUpdatePrice);
    listItemToAdd.querySelector("button.btn-del").addEventListener("click", deleteMenu);
    listItemToAdd.querySelector("button#updateNameBtn").addEventListener("click", updateName);
    listItemToAdd.querySelector("button#updatePriceBtn").addEventListener("click", updatePrice);

    menuNameInput.value = "";
    menuPriceInput.value = "";

    document.getElementById("create-menu-input-group").classList.add("d-none")
    // createMenu.classList.add("d-none");
    if (!itemNoneFoundDiv.classList.contains("d-none")) {
        itemNoneFoundDiv.classList.add("d-none");
    }
})

