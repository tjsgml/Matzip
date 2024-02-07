/*
 * signup.html 포함
 * 
 * 닉네임 중복 체크-
 * 이메일 중복 체크-
 */

document.addEventListener('DOMContentLoaded', function() {
	//유효성 검사 완료 항목 체크
	let nickChecked = false;
	let emailChecked = false;
	
	const inputNickname = document.querySelector('input#nickname');
	inputNickname.addEventListener('change', checkNickname);


	const inputEmail = document.querySelector('input#email');
	inputEmail.addEventListener('change', checkEmail);

	const btnSubmit = document.querySelector('input#btnSubmit');


	//메서드 정의 ----------------------------------------------------------------------------------------------------

	//닉네임 중복체크
	async function checkNickname(e) {
		const nickname = e.target.value.trim();
		const nicknameDiv = document.querySelector('div#nickname-check');

		const regExp = /^[a-zA-Zㄱ-ㅎ가-힣0-9_]{2,10}$/;
		if (!regExp.test(nickname)) {
			nicknameDiv.innerHTML = '2~10자의 한글, 영문 대/소문자,</br>숫자와 특수기호(_)만  사용해 주세요. (공백 사용 불가)'
			return;
		}

		const uri = `checknick?nickname=${nickname}`;
		const res = await axios.get(uri);


		if (res.data === 'Y') {
			nicknameDiv.innerHTML = '사용할 수 없는 닉네임입니다. 다른 닉네임을 입력해 주세요.';
			nickChecked = false;
		} else {
			nicknameDiv.innerHTML = '';
			nickChecked = true;
		}

		if (nickChecked && emailChecked) {
			btnSubmit.disabled = false;
		}
	}


	//이메일 중복체크
	async function checkEmail(e) {
		const email = e.target.value.trim();
		const emailDiv = document.querySelector('div#email-check');

		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

		if (!regExp.test(email)) {
			emailDiv.innerHTML = '이메일 주소가 정확한지 확인해 주세요.';
			return;
		}

		const uri = `checkemail?email=${email}`;
		const res = await axios.get(uri);


		if (res.data === 'Y') {
			emailDiv.innerHTML = '사용할 수 없는 이메일입니다. 다른 이메일을 입력해 주세요.';
			emailChecked = false;
		} else {
			emailDiv.innerHTML = '';
			emailChecked = true;
		}

		if (nickChecked && emailChecked) {
			btnSubmit.disabled = false;
		}
	}

});