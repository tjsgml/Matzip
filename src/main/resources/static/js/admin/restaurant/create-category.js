const showAddCategoryBtn = document.querySelector("button#show-add-category");
const categoryComp = document.querySelector("div#category-comp");

showAddCategoryBtn.addEventListener("click", canShowCategoryComp);
document.querySelector("button#create-category-btn").addEventListener("click", submitCategoryToAdd);
document.querySelector("input#categoryInput").addEventListener("keyup", (e) => {
    if (e.keyCode === 13) {
        submitCategoryToAdd();
    }
})

function canShowCategoryComp() {
    if (categoryComp.classList.contains("d-none")) {
        categoryComp.classList.remove("d-none");
    } else {
        categoryComp.classList.add("d-none")
    }
}

async function submitCategoryToAdd() {
    const categoryName = document.querySelector("input#categoryInput").value;

    if (!categoryName && categoryName.trim().length < 1) {
        alert("유효한 카테고리 이름으로 작성해주세요.");
        return;
    }

    const {data, status} = await axios.post("/admin/matzip/category?categoryName=" + categoryName);

    const categoryOptionToCreate = document.createElement("option");

    categoryOptionToCreate.value = data.id;
    categoryOptionToCreate.innerHTML = data.name;

    categorySelect.appendChild(categoryOptionToCreate);
    if (status === 201) resetForm(status);
}

document.querySelector("button#reset-btn", resetForm);

function resetForm(status) {
    if (status === 201) categoryComp.classList.add("d-none");
    document.querySelector("input#categoryInput").value = "";
}