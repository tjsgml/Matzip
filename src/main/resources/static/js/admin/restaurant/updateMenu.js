const dataToDelete = [];
const compToDelete = [];
let menusToCreate = [];

const menuList = document.getElementById("menu-list");
const emptyMenuList = document.getElementById("empty-menu-list");

document.querySelectorAll(".menu-badge").forEach(el => {
    el.getElementsByClassName("btn-remove-menu")[0].addEventListener("click", () => checkMenuListEmpty(el));
});

const priceInput = document.getElementById("price-input");
const addBtn = document.getElementById("add-btn");
const resetBtn = document.getElementById("reset-btn");
const submitBtn = document.getElementById("submit-btn");
const prevBtn = document.getElementById("prevBtn");
const menuInput = document.getElementById("menu-input");

addBtn.addEventListener("click", addMenuToMenuList);

function addMenuToMenuList() {

    const reg = /[0-9]+/g;

    if (menuInput.value.length <= 0 || !priceInput.value.trim().match(reg)) {
        alert("메뉴명과 가격을 입력해주세요!");
        return;
    }

    const menu = {
        name: menuInput.value.trim(),
        price: Number.parseInt(priceInput.value.trim())
    };

    menusToCreate.push(menu);

    menuList.classList.remove("d-none");
    emptyMenuList.classList.add("d-none");

    const menuItem = document.createElement("div");
    menuItem.classList.add("menu-badge");

    menuItem.innerHTML = `<div class="menu-name" ><span class="menu">${menu.name}</span><span class="badge text-bg-warning" style="margin-left: 5px">New</span></div>
                          <div class="price"><span id="price-value">${menu.price}</span><span>원</span>
                          <button class="btn btn-remove-menu" style="height: 30px;">x</button></div>`;

    menuList.append(menuItem);
    resetInput();

    menuItem.getElementsByClassName("btn-remove-menu")[0].addEventListener("click", () => checkMenuListEmpty(menuItem));
}

function checkMenuListEmpty(comp) {
    console.log(comp);

    const checkDouble = confirm("메뉴를 삭제하시겠습니까?");
    if (!checkDouble) {
        return;
    }

    if (comp.hasAttribute("data-id")) {
        const dataId = Number.parseInt(comp.getAttribute("data-id"));
        dataToDelete.push(dataId);
        compToDelete.push(comp);
        comp.classList.add("d-none");
    } else {
        const menuName = comp.getElementsByClassName("menu")[0].innerHTML.trim();
        console.log(`menuName=${menuName}`);
        const price = Number.parseInt(comp.querySelector("#price-value").innerHTML.trim());
        console.log(`price=${price}`);

        menuList.removeChild(comp);
        menusToCreate = menusToCreate.filter(el => el.name === menuName);
    }

    if (document.getElementsByClassName("menu-badge").length === 0) {
        emptyMenuList.classList.remove("d-none");
        menuList.classList.add("d-none")
    }
}

resetBtn.addEventListener("click", resetInput);

function resetInput() {
    menuInput.value = "";
    priceInput.value = "";
}

prevBtn.addEventListener("click", () => {
    if (compToDelete.length === 0) return;

    dataToDelete.pop();
    const compToReserve = compToDelete.pop();
    compToReserve.classList.remove("d-none");
})

submitBtn.addEventListener("click", async () => {
    const checkDouble = confirm("변경 사항을 적용하시겠습니까?");

    if (!checkDouble) {
        return;
    }
    let result = null;
    if (menusToCreate.length > 0) {

        result = await axios.post("./menu", {
            menus: menusToCreate
        }, {
            headers: {
                "Content-Type": "application/json"
            }
        });


    }

    if (result && result.status !== 201) {
        return;
    }

    let query = "";
    let resp = null;
    if (dataToDelete.length > 0) {
        dataToDelete.forEach(el => {
            query += (query === "") ? ("menus=" + el) : ("&menus=" + el);
        })

        resp = await axios.delete("../menu?" + query);
    }

    if (resp && resp.status !== 204) {
        return;
    }

    location.reload();
    console.dir("result : " + result);
})
