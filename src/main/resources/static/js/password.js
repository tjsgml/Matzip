/**
 * password.html 포함
 * 비밀번호 유효성 체크
 */

document.addEventListener('DOMContentLoaded', function() {
	let isPwdValid = false;
	let firstSubmit = false;
	
	console.log(isLinkValid);
	
	const pwdForm = document.querySelector('form.pwd-form');

	const passwordDiv = document.querySelector('div#password-check');

	const btnSubmit = document.querySelector('button.pwd-form__btn');
	btnSubmit.addEventListener('click', changePwd);

	const inputPwd = document.querySelector('input#password');
	inputPwd.addEventListener('keyup', regExpTest);

	const inputDoublePwd = document.querySelector('input#passwordcheck__input');
	inputDoublePwd.addEventListener('keyup', interDoublePwdCheck);

	//비밀번호 유효성
	const regExp = /^[a-zA-Z0-9_!@#$&*]{8,16}$/;

	//새 비밀번호로 변경 - 버튼 클릭
	function changePwd() {
		const password = inputPwd.value.trim();
		const doublePwd = inputDoublePwd.value.trim();

		firstSubmit = true;

		console.log('password: ', password, ' doublePassword : ', doublePwd);
		conditional(password, doublePwd);

		if(isLinkValid === 'N'){
			passwordDiv.innerHTML = '비밀번호 변경 실패 : ConfirmEmail not found';
			return;
		}

		if (isPwdValid) {
			//console.log('제출');
			pwdForm.submit();
		}
	}

	//
	function interDoublePwdCheck(e) {
		const password = inputPwd.value.trim();
		const doublePwd = e.target.value.trim();

		if (firstSubmit) {
			conditional(password, doublePwd);
		}
	}

	//
	function regExpTest() {
		const password = inputPwd.value.trim();
		const doublePwd = inputDoublePwd.value.trim();
		if (firstSubmit) {
			if (!regExp.test(password)) {
				passwordDiv.innerHTML = '8~16자의 영문 대/소문자, 숫자, 특수문자(_!@#$&*)를 사용해 주세요.';
				isPwdValid = false;
				return;
			} else if (password !== doublePwd) {
				passwordDiv.innerHTML = '비밀번호가 서로 일치하지 않습니다.';
				isPwdValid = false;
				return;
			} else {
				passwordDiv.innerHTML = '';
				isPwdValid = true;
			}
		}
	}


	//
	function conditional(password, doublePwd) {
		if (password !== doublePwd) {
			passwordDiv.innerHTML = '비밀번호가 서로 일치하지 않습니다.';
			isPwdValid = false;
			return;
		} else if (!regExp.test(password) || !regExp.test(doublePwd)) {
			passwordDiv.innerHTML = '8~16자의 영문 대/소문자, 숫자, 특수문자(_!@#$&*)를 사용해 주세요.';
			isPwdValid = false;
			return;
		} else {
			passwordDiv.innerHTML = '';
			isPwdValid = true;
		}
	}
})