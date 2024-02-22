/**
 * profilemodify.html 포함
 * 프로필 이미지, 기본 변경 및 앨범에서 선택하여 변경 할 수 있도록 
 * 모달을 띄어 선택할 수 있도록 함.
 */

document.addEventListener('DOMContentLoaded', function() {
	const modal = new bootstrap.Modal('div#proImgModal', { backdrop: true });
	const profileImg = document.querySelector('div.modify-pic>img');

	//프로필 변경 모달 띄우기
	const btnImgSelect = document.querySelector('a#selectImg');
	btnImgSelect.addEventListener('click', (e) => { modal.show(); })

	//모달 [기본 이미지로 변경] 클릭 이벤트 리스너 등록
	const inputDefaultImg = document.querySelector('div.defaultImg');
	inputDefaultImg.addEventListener('click', changeDefaultImg);

	//모달[앨범에서 선택] 클릭 모달 창 지우기
	const inputCustomImg = document.querySelector('div.customImg');
	inputCustomImg.addEventListener('click', (e) => { modal.hide(); });

	//input 파일에 변화가 생기면 이벤트 리스너
	const imgFile = document.querySelector('input#customProfile');
	imgFile.addEventListener('change', changeCutsomImg);

	//------------------------------------------------------------------------
	//기본 이미지로 변경
	async function changeDefaultImg() {
		modal.hide();
		try {
			const response = await axios.get('../memberinfo/changeDefaultImg');

			profileImg.src = response.data;

			alert("기본 이미지로 변경되었습니다.");
		} catch (error) {
			console.log(error);
		}
	}

	//프로필 이미지 커스텀 변경
	async function changeCutsomImg(e) {
		const imgFile = e.target;

		if (!imgFile) {
			return;
		}

		const formData = new FormData();
		formData.append('file', imgFile.files[0]);

		try {
			const response = await axios.post('../memberinfo/changeCtmImg', formData, {
				headers: {
					'Content-Type': 'multipart/form-data'
				}
			});
			profileImg.src = response.data;

			alert("프로필 사진이 변경되었습니다.");
		} catch (error) {
			console.log(error);
		}
	}
})