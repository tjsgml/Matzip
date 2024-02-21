/**
 * profilemodify.html 포함
 * 프로필 이미지, 기본 변경 및 앨범에서 선택하여 변경 할 수 있도록 
 * 모달을 띄어 선택할 수 있도록 함.
 */

document.addEventListener('DOMContentLoaded', function() {
	const modal = new bootstrap.Modal('div#proImgModal', { backdrop: true });

	//프로필 변경 모달 띄우기
	const btnImgSelect = document.querySelector('a#selectImg');
	btnImgSelect.addEventListener('click', functionImgSelect)

	//모달 [기본 이미지로 변경] 클릭 이벤트 리스너 등록
	const inputDefaultImg = document.querySelector('div.defaultImg');
	inputDefaultImg.addEventListener('click', changeDefaultImg);


	//------------------------------------------------------------------------
	function functionImgSelect() {
		modal.show();
	}

	async function changeDefaultImg() {

		try {
			const response = await axios.get('../memberinfo/changeDefaultImg/{imgfile}');

			makeImgShowModal(response.data);

		} catch (error) {
			console.log(error);
		}


		modal.hide();
	}
})