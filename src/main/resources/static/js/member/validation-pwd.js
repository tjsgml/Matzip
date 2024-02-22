/**
 * pwdmodify.html 포함
 * 비밀번호 유효성 검사 및 
 * 현재 비밀번호와 새로운 비밀번호 검사
 */

document.addEventListener('DOMContentLoaded', function() {
	let oldPwdChecked = false;
	let newPwdChecked = false;

	const inputOldPwd = document.querySelector('input#oldPwd');
	const inputNewPwd = document.querySelector('input#newPwd');

	const divOldPwd = document.querySelector('div#oldPwd-check');
	const divNewPwd = document.querySelector('div#newPwd-check');

	const modifyForm = document.querySelector('form#pwdModifyForm');
	const btnSave = document.querySelector('button#modifyBtn');


	inputOldPwd.addEventListener('keyup', checkCurrentPwd);
	inputNewPwd.addEventListener('keyup', checkNewPwd);
	btnSave.addEventListener('click', submitForm);

	//------------------------------------------------------------------------
	async function checkCurrentPwd(e) {
		const oldPwd = e.target.value.trim();

		if (oldPwd == '') {
			return;
		}

		try {
			const res = await axios.post('../member/checkpwd', oldPwd);

			if (res.data == 'N') {
				divOldPwd.innerHTML = '비밀번호가 맞지 않습니다.';
				oldPwdChecked = false;
			} else {
				divOldPwd.innerHTML = '';
				oldPwdChecked = true;
			}
		} catch (error) {
			console.log(error);
		}
	}

	function checkNewPwd(e) {
		const newPwd = e.target.value;

		const regExp = /^[a-zA-Z0-9_!@#$&*]{8,16}$/;

		if (!regExp.test(newPwd)) {
			divNewPwd.innerHTML = '8~16자의 영문 대/소문자, 숫자, 특수문자(_!@#$&*)를 사용해 주세요.';
			newPwdChecked = false;
		} else {
			divNewPwd.innerHTML = '';
			newPwdChecked = true;
		}
	}

	function submitForm() {
		if (oldPwdChecked && newPwdChecked) {
			modifyForm.submit();
		}
	}
})