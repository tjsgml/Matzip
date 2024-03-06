const reviewComp = document.querySelector("div#review-comp");
const updateAuthorizationComp = document.querySelector("div#update-authorization");
const btnShowReviewList = document.querySelector("button#btn-show-review-list");
const btnUpdateAuthorization = document.querySelector("button#btn-update-authorization");
const reviewList = document.querySelector("ul#review-list");
const paginationList = document.querySelector("ul.pagination");

let page = 0;

const reviewUrl = new URL(window.location.href)
const review = reviewUrl.searchParams.get("review");

if (review === "Y") {
    configReviewList();
    reviewComp.classList.remove("d-none");
}

btnShowReviewList.addEventListener("click", () => {
    if (!reviewComp.classList.contains("d-none")) {
        reviewComp.classList.add("d-none");
    } else {
        page = 0;
        reviewComp.classList.remove("d-none");
    }
    configReviewList();
    updateAuthorizationComp.classList.add("d-none");
})

btnUpdateAuthorization.addEventListener("click", () => {
    if (!updateAuthorizationComp.classList.contains("d-none")) {
        updateAuthorizationComp.classList.add("d-none");
    } else {
        updateAuthorizationComp.classList.remove("d-none");
    }
    reviewComp.classList.add("d-none");
})

async function configReviewList() {
    reviewList.innerHTML = "";
    const {data} = await axios.get(location.origin + location.pathname + `/review?curPage=${page}`);
    const {totalPages, number, content: listItems} = data;

    if (!listItems || !listItems.length) {
        reviewComp.innerHTML = `<div class="container card mt-4 border-0"
                             style="border-radius: 24px; box-shadow: 0px 0px 10px 0px rgb(230, 230, 230);">
                            <div id="item-none-found" class="card-body py-5">
                                <div class="row fw-semibold text-center"
                                     style="font-size: 20px;">
                                    <div class="col my-5">
                                        <p style="font-size: 50px">💬</p>
                                        <div>
                                            해당 유저가 작성한 리뷰가 없습니다.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>`;

        return;
    }

    renderPagination(paginationList, totalPages, number, configReviewList, page);

    listItems.forEach(el => {
        const listItem = document.createElement("li");
        listItem.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-start");

        const createdTime = new Date(el.createdTime);

        const imgs = el.reviewImages?.map(image => `
                <div class="d-inline-block position-relative">
                    <button class="btn btn-sm btn-warning btn-delete-image position-absolute top-0 end-0" data-id="${image.id}">X</button>
                    <img src="${image.imgUrl}" alt="이미지"/>
                </div>`).join("");

        listItem.innerHTML = `<div class="ms-2 me-auto">
                                    <div>${createdTime.getFullYear()}년 ${String(createdTime.getMonth()).padStart(2, 0)}월 ${String(createdTime.getDay()).padStart(2, 0)}일</div>
                                    <div>
                                        <span class="detail_rating">맛
                                            <img src=${el.flavorScore ? "/img/star_on.png" : "/img/star_off.png"} class="miniStar"/>${el.flavorScore}
                                        </span>
                                        <span class="detail_rating">서비스
                                            <img src=${el.flavorScore ? "/img/star_on.png" : "/img/star_off.png"} class="miniStar"/>${el.serviceScore}
                                        </span>
                                        <span class="detail_rating">가격
                                            <img src=${el.flavorScore ? "/img/star_on.png" : "/img/star_off.png"} class="miniStar"/>${el.priceScore}
                                        </span>
                                    </div>
                                    <div class="review-img-box">
                                         ${imgs ?? ""}
                                    </div>
                                    <div class="mt-3">
                                        ${el.content}
                                    </div>
                               </div>
                               <button class="btn-delete-review btn badge text-bg-primary rounded-pill" data-id="${el.id}">삭제</button>
                            `;

        reviewList.append(listItem);
    });

    document.querySelectorAll("button.btn-delete-image").forEach(el => {
        el.addEventListener("click", deleteImg);
    })

    document.querySelectorAll("button.btn-delete-review").forEach(el => {
        el.addEventListener("click", deleteReview);
    })
}


async function deleteImg(e) {
    if (!confirm("해당 이미지를 삭제하시겠습니까?")) {
        alert("이미지 삭제가 취소되었습니다.");
        return;
    }

    const imgId = e.target.getAttribute("data-id");

    const {status} = await axios.delete(`./review/img/${imgId}`);

    if (status !== 204) {
        alert("이미지를 삭제하는데 실패하였습니다. 다시 시도해주세요!");
        return;
    }

    const imgsContainer = e.target.parentNode.parentNode;
    const imgBox = e.target.parentNode;

    imgsContainer.removeChild(imgBox);
}

async function deleteReview(e) {

    if (!confirm("해당 리뷰를 삭제하시겠습니까?")) {
        alert("리뷰가 삭제가 취소되었습니다.");
        return;
    }
    const reviewId = e.target.getAttribute("data-id");

    const {status} = await axios.delete(`./review/${reviewId}`);

    if (status !== 204) {
        alert("리뷰를 삭제하는데 실패하였습니다. 다시 시도해주세요!");
        return;
    }

    const reviewItem = e.target.parentNode;
    reviewList.removeChild(reviewItem);
}

const roleCheckBoxes = document.querySelectorAll("input[type='checkbox'].form-check-input");
const modifyRoleBtn = document.querySelector("button#modify-role-btn");

modifyRoleBtn.addEventListener("click", async () => {
    const rolesToModify = [];

    roleCheckBoxes.forEach(el => {
        if (el.checked) {
            rolesToModify.push(el.value);
        }
    })

    if (rolesToModify.length <= 0) {
        console.log("최소 1개의 역할을 부여해주세요");
        return;
    }

    const {status} = await axios.patch(location.href + "/role", rolesToModify);
    location.reload();
})