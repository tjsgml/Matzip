/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
	const totalRating = document.querySelector('strong.totalRating').innerHTML;
	const startsDiv = document.querySelector('div.stars');
	let starsHtml='';

	for (let index = 1; index <= 5; index++) {
		if (index <= Math.floor(totalRating)) {
			starsHtml += `<img src="/img/star_on.png" />`;
		} else if (index === Math.ceil(totalRating) && totalRating % 1 >= 0.5) {
			starsHtml += `<img src="/img/star_half.png">`;
		} else {
			starsHtml += `<img src="/img/star_off.png" />`;
		}
	}
	
	startsDiv.innerHTML = starsHtml;

})