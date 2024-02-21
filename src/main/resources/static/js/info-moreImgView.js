/**
 * myreview.html 포함
 * 이미지 더 크게 볼 수 있는 모달 띄우기
 */

document.addEventListener('DOMContentLoaded', function() {
	const modal = new bootstrap.Modal('div#gallryModal', { backdrop: true });

	const showImgMore = document.querySelectorAll('a.imgEnl');
	for(let abtn of showImgMore){
		abtn.addEventListener('click', functionShowMoreImg);
	}
	
	
	//----------------------------------------------
	async function functionShowMoreImg(e){
		const reviewId = e.target.getAttribute('data-id');
		console.log("리뷰 아이디 : ", reviewId);
		
		try{
			const response = await axios.get(`../review/img/${reviewId}`);
			
			makeImgShowModal(response.data);
			
		}catch(error){
			console.log(error);
		}
		modal.show();
	}
	
	function makeImgShowModal(data){
		const divImg = document.querySelector('div.imgContainer_img');
		let htmlStr = '';
		
		for(let d of data){
			htmlStr += `<img src=${d} style="width:90%; height:90%" />`
		}
		
		divImg.innerHTML = htmlStr;
		
		console.log(htmlStr);
	}
})