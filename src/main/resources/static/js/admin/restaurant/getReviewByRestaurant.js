const pagination = document.querySelector("nav#pagination");
const paginationList = pagination.querySelector("#pagination>ul.pagination");
const reviewList = document.querySelector("tbody#review-list");
let page = 0;

document.addEventListener("DOMContentLoaded", async () => {
    resetReviewTable(reviewList);
    renderReviewList(reviewList);
})


function resetReviewTable(reviewList) {
    reviewList.innerHTML = "";
}

async function renderReviewList() {
    const {data} = await axios.get(`./${restaurantId}/reviews?page=${page}`);
    const {content, totalPages, number} = data;

    if (content == null || content.length === 0) {
        reviewList.classList.add("d-none");
        return;
    }

    content.forEach(el => {
        const tr = mkTableRow(el);
        reviewList.appendChild(tr);
    });

    renderPagination(paginationList, totalPages, number, renderReviewList, page);
}

function mkTableRow(el) {
    const tableRow = document.createElement("tr");
    const innerHtml = `
        <td>
            <div id="user-info" class="p-3 d-flex justify-content-between">
                <div class="p-3 d-flex justify-content-start">
                    <div class="mt-3">
                        <img style="width: 50px;" src="${el.member.img}"/>
                    </div>
                    <div class="d-inline-block">
                        <div>${el.member.username}</div>
                        <div class="text-secondary">
                            ${el.createdTime.split("T")[0]}
                        </div>
                        <div>
                            <span class="me-2">맛 <img src="/img/miniStar.png"/> 5</span>
                            <span class="me-2">가격 <img src="/img/miniStar.png"/> 4</span>
                            <span class="me-2">서비스 <img src="/img/miniStar.png"/> 5</span>
                        </div>
                    </div>
                </div>
                <div>
                    <button class="btn-delete-review btn btn-outline-primary btn-sm" data-id="${el.id}">삭제</button>
                </div>
            </div>
            <div id="img-box" style="text-align: center">
                ${el.reviewImages.map(img => '<img style="width: 150px;display: inline-block" src="' + img.imgUrl + '"/>')}
            </div>
            <div id="review-info" class="p-4">
               ${el.content}
            </div>
        
        </td>
    `;

    tableRow.innerHTML = innerHtml;
    return tableRow;
}

function addDeleteBtnEvent (reviewList) {
    const delBtns = reviewList.querySelector("button.btn-delete-review");

    delBtns.forEach(el => {
        el.addEventListener("click", (e) => {
            const reviewId = e.target.getAttribute("data-id");

            if (!confirm("정말 삭제하시겠습니까?")) {
                alert("리뷰 삭제가 취소되었습니다.")
                return;
            }


        });
    })
}


