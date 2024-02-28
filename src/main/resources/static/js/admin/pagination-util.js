function renderPagination(totalPages, curPage, func, varPrim) {
    paginationList.innerHTML = "";
    mkPaginationListItem(0, curPage, "≪", func, varPrim);

    for (let i = 0; i < totalPages; i++) {
        mkPaginationListItem(i, curPage, i + 1, func, varPrim);
    }

    mkPaginationListItem(totalPages - 1, curPage, "≫", func, varPrim);
}

function mkPaginationListItem(num, curPage, txt, func, varPrim) {
    const paginationListItem = document.createElement("li");
    paginationListItem.classList.add("page-item");
    if (curPage === num) paginationListItem.classList.add("disabled");

    paginationListItem.innerHTML = `
    <button class="page-link" ${curPage === num ? "disabled" : ""}>${txt}</button>
    `;

    paginationList.appendChild(paginationListItem);

    paginationListItem.querySelector("button.page-link").addEventListener("click", () => {
        if (varPrim === undefined) query.page = num;
        else page = num;
        func();
    });
}
