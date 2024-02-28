/**
 * @param paginationList 페이지네이션이 저장될 <ul> HTML Element
 * @param totalPages 총 페이지
 * @param curPage 현재 페이지
 * @param func 리스트를 그리는 메서드
 * @param varPrim 현재 페이지를 저장하는
 */

function renderPagination(paginationList, totalPages, curPage, func, varPrim) {
    paginationList.innerHTML = "";
    mkPaginationListItem(paginationList, 0, curPage, "≪", func, varPrim);

    for (let i = 0; i < totalPages; i++) {
        mkPaginationListItem(paginationList, i, curPage, i + 1, func, varPrim);
    }

    mkPaginationListItem(paginationList, totalPages - 1, curPage, "≫", func, varPrim);
}

function mkPaginationListItem(paginationList, num, curPage, txt, func, varPrim) {
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
