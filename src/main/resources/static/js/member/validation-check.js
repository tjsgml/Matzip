/**
 * profilemodify.html 포함
 * 닉네임, 이메일 유효성 검사 확인
 */

document.addEventListener('DOMContentLoaded', function() {
	//유효성 검사 완료 항목
	let nickChecked = true;
	let emailChecked = true;

	//input 태그 정보 갖고오기
	const inputNickname = document.querySelector('input#nickname');
	const inputEmail = document.querySelector('input#email');

	//저장된 내 아이디와 이메일
	const firstNickname = inputNickname.value;
	const firstEmail = inputEmail.value;

	//유효성 체크 문구 표시 div
	const divNickname = document.querySelector('div#nickname-check');
	const divEmail = document.querySelector('div#email-check');

	//저장 버튼
	const btnSave = document.querySelector('button#modifyBtn');
	//폼
	const modifyForm = document.querySelector('form#memberinfoModifyForm');


	//닉네임 유효성 검사
	inputNickname.addEventListener('change', checkNickname);
	//이메일 유효성 검사
	inputEmail.addEventListener('change', checkEmail);
	//유효성 검사 모두 완료되면 폼 제출
	btnSave.addEventListener('click', submitForm);


	//----------------------------------------------------------------------------------------------
	//닉네임 중복체크
	async function checkNickname(e) {
		const nickname = e.target.value.trim();

		if (firstNickname == nickname) {
			divNickname.innerHTML = '';
			nickChecked=true;
			return;
		}

		const regExp = /^[a-zA-Zㄱ-ㅎ가-힣0-9_]{2,10}$/;
		if (!regExp.test(nickname)) {
			divNickname.innerHTML = '2~10자의 한글, 영문 대/소문자, 숫자와 특수기호(_)만  사용해 주세요. (공백 사용 불가)';
			nickChecked=false;
			return;
		}

		const uri = `../member/checknick?nickname=${nickname}`;
		const res = await axios.get(uri);

		if (res.data === 'Y') {
			divNickname.innerHTML = '사용할 수 없는 닉네임입니다. 다른 닉네임을 입력해 주세요.';
			nickChecked = false;
		} else {
			divNickname.innerHTML = '';
			nickChecked = true;
		}
	}


	//이메일 중복체크
	async function checkEmail(e) {
		const email = e.target.value.trim();

		if (firstEmail == email) {
			divEmail.innerHTML = '';
			emailChecked=true;
			return;
		}

		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		if (!regExp.test(email)) {
			divEmail.innerHTML = '이메일 주소가 정확한지 확인해 주세요.';
			emailChecked=false;
			return;
		}

		const uri = `../member/checkemail?email=${email}`;
		const res = await axios.get(uri);

		if (res.data === 'Y') {
			divEmail.innerHTML = '사용할 수 없는 이메일입니다. 다른 이메일을 입력해 주세요.';
			emailChecked = false;
		} else {
			divEmail.innerHTML = '';
			emailChecked = true;
		}
	}

	//폼 제출하기
	function submitForm() {
		if(nickChecked&&emailChecked){
			modifyForm.submit();
		}
	}
})