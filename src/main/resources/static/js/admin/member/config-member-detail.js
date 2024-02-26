const reviewComp = document.querySelector("div#review-comp");
const updateAuthorizationComp = document.querySelector("div#update-authorization");
const btnShowReviewList = document.querySelector("button#btn-show-review-list");
const btnUpdateAuthorization = document.querySelector("button#btn-update-authorization");
const reviewList = document.querySelector("ul#review-list");

let curPage = 0;

btnShowReviewList.addEventListener("click", () => {
    if (!updateAuthorizationComp.classList.contains("d-none")) {
        updateAuthorizationComp.classList.add("d-none");
    }

    configReviewList();
    reviewComp.classList.remove("d-none");
})

btnUpdateAuthorization.addEventListener("click", () => {
    if (!reviewComp.classList.contains("d-none")) {
        reviewComp.classList.add("d-none");
    }

    updateAuthorizationComp.classList.remove("d-none");
})

async function configReviewList() {
    reviewList.innerHTML = "";

    const {data} = await axios.get(location.href + "/review?curPage=" + curPage);
    data.forEach(el => {
        const listItem = document.createElement("li");
        listItem.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-start");

        const imgs = el.reviewImages.map(image => `
                <div class="d-inline-block position-relative">
                    <button class="position-absolute top-0 end-0" data-id="${image.id}">X</button>
                    <img src="${image.imgUrl}" alt="이미지"/>
                </div>`).join("")

        listItem.innerHTML = `<div class="ms-2 me-auto">
                                    <div>1997년 02월 49일</div>
                                    <div>
                                        <span class="detail_rating">맛
                                            <img src="/img/miniStar.png" class="miniStar"/>3
                                        </span>
                                        <span class="detail_rating">서비스
                                            <img src="/img/miniStar.png" class="miniStar"/>3
                                        </span>
                                        <span class="detail_rating">가격
                                            <img src="/img/miniStar.png" class="miniStar"/>3
                                        </span>
                               </div>
                               <div class="review-img-box">
                                    ${imgs}
                               </div>
                               <div class="mt-3">
                                   토요일 점심으로 찾았다.
                                   식당에 도착하기 40분전에 테이블링으로 원격줄서기를 했음에도 도착하고 나서도 20분을 더 기다렸다.
                                   외관에서도 살짝 느껴지지만 안에 들어서면 미국미국한 인테리어가 반긴다.
                                   요즘 핫한 미국보다는 그 어떤 스테레오타입의 미국풍이 물씬 나는 뉴트로 느낌
                               </div>
                               </div>
                               <button class="btn badge text-bg-primary rounded-pill">삭제</button>
                            `;

        reviewList.append(listItem);
    });
}