/**
 * home.html 포함
 * 레스토랑 캐러셀 기능 구현
 */

document.addEventListener('DOMContentLoaded', function() {
	const boxes = document.querySelectorAll('div.tag-restbox');

	const cnt = [];
	const container = [];
	const carouselLength = [];

	for (let box of boxes) {
		const num = box.getAttribute('data-id');
		cnt[num] = 0;

		container[num] = box.querySelector('ul');
		carouselLength[num] = box.querySelector('ul').children.length;

		const prevBtn = box.querySelector('button.preBtn');
		const nextBtn = box.querySelector('button.nextBtn');

		prevBtn.addEventListener("click", movetoLeft);
		nextBtn.addEventListener("click", movetoRight);
	}

	//--------------------------------------------------------------------------------------------
	//왼쪽 버튼을 클릭 시 동작
	function movetoLeft(e) {
		const num = e.target.getAttribute('data-id');
		if (cnt[num] != 0) {
			cnt[num]++;
			translateContainer(num);
		}
	}

	//오른쪽 버튼을 클릭 시 동작
	function movetoRight(e) {
		const num = e.target.getAttribute('data-id');

		if (cnt[num] != -carouselLength[num] + 3 && carouselLength[num] > 3) {
			cnt[num]--;
			translateContainer(num);
		}
	}

	//ul의 위치를 이동시킴
	function translateContainer(num) {
		container[num].style.transform = `translateX(${(215 * cnt[num])}px)`;
	}

});
