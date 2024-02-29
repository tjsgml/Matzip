/**
 * mymain.html 포함
 * 리뷰 리스트 데이터 가져옴
 * 무한 스크롤 기능 구현 자바 스크립트
 * 이미지 더 크게 볼 수 있는 모달 띄우기
 */

document.addEventListener('DOMContentLoaded', function() {
	const post_list = document.querySelector("section.mypick-list");		//부모 태그
	let post_one = document.querySelector("div.mypick-post_one:last-child");		//마지막 위치에 있는 자식 태그

	const pickCnt = document.querySelector('dt#pickCnt');	//마이 픽 수

	getMyPickList(0);


	const io = new IntersectionObserver(
		(entry, observer) => {
			const ioTarget = entry[0].target;

			if (entry[0].isIntersecting) {
				const count = post_list.children.length;	//현재 부모 태그가 가지고 있는 자식 수

				if (pickCnt.innerHTML >= count) {
					console.log("현재 보이는 타겟", ioTarget);

					io.unobserve(post_one);		//현재 태그의 옵저버를 끊음

					//리뷰 1개 가져오기
					getMyPickList(count);
				}
			}
		},
		{
			rootMargin: '10px',
			threshold: 1,
		}
	);



	//----------------------------------------------------------
	//북마크 가져오기
	async function getMyPickList(cnt) {
		try {
			const res = await axios.get(`../memberinfo/api/mypick?p=${cnt}`);

			makeHtmlPostOne(res.data.content[0]);

			post_one = document.querySelector("div.mypick-post_one:last-child");
			io.observe(post_one);		//새 자식 옵저버 연결

		} catch (error) {
			console.log(error);
		}
	}

	//포스트 한개 html 그리기
	function makeHtmlPostOne(data) {
		let strHtml = '';
		strHtml += `<!-- 포스트 1개-->
						<div class="mypick-post_one margin-side-lr">
							<!-- 음식점 사진 -->
							<a href="{/rest/details?id=${data.restaurantId}}">`;

		if (data.imgUrl != null) {
			strHtml += `<div>
									<img src="${data.imgUrl}" />
								</div>`;
		} else {
			strHtml += `<div>
									<div class="emptyImg">
										<img src="/img/reviewNullImg.png"/>
									</div>
								</div>`;
		}

		strHtml += `</a>

							<!-- 음식점 정보 -->
							<div class="mypick-detail">
								<a href="/rest/details?id=${data.restaurantId}">
									<h4>${data.restaurantName}</h4>
									<div class="mypick-rating">
										<img src="/img/v26_89.png" />
										<div>${data.totalSart}</div>
										<div>(<span>${data.reviewAllCount}</span>)</div>
									</div>
									<div class="mypick-category">
										<span>${data.categoryName}</span>
										<span>.</span>
										<span>${data.location}</span>
									</div>
								</a>
							</div>

							<!-- 북마크 -->
							<div class="mypick-pick">
								<a href="/memberinfo/delete?id=${data.pickId}">
									<img src="/img/icon_myPickOn.png" />
								</a>
							</div>
						</div>`;

		post_list.innerHTML += strHtml;
	}
})