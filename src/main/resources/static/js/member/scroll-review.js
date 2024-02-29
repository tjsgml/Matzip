/**
 * mymain.html 포함
 * 리뷰 리스트 데이터 가져옴
 * 무한 스크롤 기능 구현 자바 스크립트
 * 이미지 더 크게 볼 수 있는 모달 띄우기
 */

document.addEventListener('DOMContentLoaded', function() {
	const post_list = document.querySelector("section.myreview-list");		//부모 태그
	let post_one = document.querySelector("div.review-post:last-child");		//마지막 위치에 있는 자식 태그

	const reviewCnt = document.querySelector('dt#reviewCnt');	//리뷰 수

	const modal = new bootstrap.Modal('div#gallryModal', { backdrop: true });

	getReveiwList(0);


	const io = new IntersectionObserver(
		(entry, observer) => {
			const ioTarget = entry[0].target;

			if (entry[0].isIntersecting) {
				const count = post_list.children.length;	//현재 부모 태그가 가지고 있는 자식 수

				if (reviewCnt.innerHTML >= count) {
					console.log("현재 보이는 타겟", ioTarget);

					io.unobserve(post_one);		//현재 태그의 옵저버를 끊음

					//리뷰 1개 가져오기
					getReveiwList(count);
				}
			}
		},
		{
			rootMargin: '10px',
			threshold: 1,
		}
	);



	//----------------------------------------------------------
	//리뷰들 가져오기
	async function getReveiwList(cnt) {
		try {
			const res = await axios.get(`../memberinfo/api/myreview?p=${cnt}`);

			makeHtmlPostOne(res.data.content[0]);

			//a태그에 이벤트 리스너 등록하기
			const showImgMore = document.querySelectorAll('a.imgEnl');
			showImgMore.forEach((btn) => {
				btn.removeEventListener('click', functionShowMoreImg); // 이미 등록된 리스너가 있으면 제거.
				btn.addEventListener('click', functionShowMoreImg); // 리스너를 새로 등록.
			});

			post_one = document.querySelector("div.review-post:last-child");
			io.observe(post_one);		//새 자식 옵저버 연결

		} catch (error) {
			console.log(error);
		}
	}

	//모달 보여주기
	async function functionShowMoreImg(e) {
		const reviewId = e.target.getAttribute('data-id');

		try {
			const response = await axios.get(`../review/img/${reviewId}`);

			makeImgShowModal(response.data);

		} catch (error) {
			console.log(error);
		}
		modal.show();
	}

	//모달에 이미지 그리기
	function makeImgShowModal(data) {
		const divImg = document.querySelector('div.imgContainer_img');
		let htmlStr = '';

		for (let d of data) {
			htmlStr += `<img src=${d} style="width:90%; height:90%" />`
		}

		divImg.innerHTML = htmlStr;
	}


	//포스트 한개 html 그리기
	function makeHtmlPostOne(data) {
		//리뷰 이미지 사이즈 변수
		const reviewImgSize = data.reviewImg.length;
		let strHtml = '';

		strHtml += `<div class="review-post margin-side-lr">
							<div class="review-post_column">
								<a href="/rest/details?id=${data.restaurantId}">`;
		if (data.mainImg != null) {
			strHtml += `<img class="img-sm" src="${data.mainImg}" />`;
		} else {
			strHtml += `<div class="emptyMainImg img-sm">
			<img src="/img/reviewNullImg.png"/>
			</div>`;
		}
		strHtml += `<div class="review-info">
										<h4>${data.restaurantName}</h4>
										<div>${data.categoryName} . ${data.location}</div>
							  </div>
							  </a>
							</div>

							<!-- 이미지 뿌리는 곳 -->
							<div class="review-post_column">
								<div class="review-img_pic">`;

		if (reviewImgSize == 1) {
			strHtml += `<!-- 이미지 1개 -->
									<div class="imgContainer one-pic">
										<a class="imgEnl">
											<div class="imgContainer_box">
												<img src="${data.reviewImg[0]}" data-id="${data.reviewId}" />
											</div>
										</a>
									</div>`;
		} else if (reviewImgSize == 2) {
			strHtml += `<!-- 이미지 2개 -->
									<div class="imgContainer two-pic">
										<a class="imgEnl">`;

			for (let img of data.reviewImg) {
				strHtml += `<div class="imgContainer_box">
												<img src="${img}" data-id="${data.reviewId}" />
											</div>`;
			}
			strHtml += `</a>
									</div>`;
		} else if (reviewImgSize == 3) {
			strHtml += `<!-- 이미지 3개 -->
									<div class="imgContainer di-grid three-pic">
										<a class="imgEnl">`;
			for (let img of data.reviewImg) {
				strHtml += `<div class="imgContainer_box">
												<img src="${img}" data-id="${data.reviewId}" />
											</div>`;
			}
			strHtml += `</a>
									</div>`;
		} else if (reviewImgSize == 4) {
			strHtml += `<!-- 이미지 4개 -->
									<div class="imgContainer di-grid">
										<a class="imgEnl">`;
			for (let img of data.reviewImg) {
				strHtml += `<div class="imgContainer_box">
												<img src="${img}" data-id="${data.reviewId}" />
											</div>`;
			}
			strHtml += `</a>
									</div>`;
		} else if (reviewImgSize > 4) {
			strHtml += `<!-- 이미지 4개 이상 -->
									<div class="imgContainer di-grid">
										<a class="imgEnl">`;

			for (let i = 0; i < 4; i++) {
				strHtml += `<div class="imgContainer_box more-pic">`;

				if (i == 3) {
					strHtml += `<div class="moreImg" id="moreImgView" data-id="${data.reviewId}">
													<i class="fa-solid fa-camera" data-id="${data.reviewId}"></i>+
													<span data-id="${data.reviewId}">${reviewImgSize - 4}</span>
												</div>`;
				}
				strHtml += `<img src="${data.reviewImg[i]}" data-id="${data.reviewId}" />
											</div>`;
			}
			strHtml += `</a>
									</div>`;
		}

		strHtml += `</div>
							</div><!-- 이미지 뿌리는 곳 끝나는 div -->
							
							<div class="review-post_column">
								<div class="review-rating">
									<div>
										<span>맛</span>
										<span>
											<img src="/img/v26_89.png" />
											<strong>${data.flavorScore}</strong>
										</span>
									</div>
									<div>
										<span>가격</span>
										<span>
											<img src="/img/v26_89.png" />
											<strong>${data.priceScore}</strong>
										</span>
									</div>
									<div>
										<span>서비스</span>
										<span>
											<img src="/img/v26_89.png" />
											<strong>${data.serviceScore}</strong>
										</span>
									</div>
								</div>
								<div>${data.formattedRegisterDate}</div>
							</div>
							<div class="review-post_column">
								<h3></h3>
								<pre class="review-content">${data.content}</pre>
							</div>

							<div class="review-post_column dropdown text-end">
								<div class="d-block link-body-emphasis text-decoration-none" data-bs-toggle="dropdown"
									aria-expanded="false">
									<img src="/img/icon_null.png" />
								</div>
								<ul class="dropdown-menu text-small dropdown-menu-end">
									<li>
										<a class="dropdown-item" href="#">리뷰 수정</a>
									</li>
									<li>
										<a class="dropdown-item" href="#">리뷰 삭제</a>
									</li>
								</ul>
							</div>
						</div>`

		post_list.innerHTML += strHtml;
	}
})