/**
 * forgot.html 포함
 * 가입된 이메일인지 체크
 */

document.addEventListener('DOMContentLoaded', function() {
	const form = document.querySelector('form.forgot-form');
	let hasClick = false;

	const btnBack = document.querySelector('button#btnBack');
	btnBack.addEventListener('click', backPage);

	const btnSubmit = document.querySelector('button#btnSubmit');
	btnSubmit.addEventListener('click', validEmail);

	const email = document.querySelector('input#email');
	email.addEventListener('keyup', validCheckEmail);



	//메서드 정의 ----------------------------------------------------------------------------------------------------

	//취소 버튼 클릭 시, 로그인 페이지로 이동
	function backPage(){
		location.href='login';
	};


	//이메일 유효 및 가입된 회원인지 체크
	function validEmail() {
		const emailDiv = document.querySelector('div#email-check');

		const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

		if (!regExp.test(email.value.trim())) {
			emailDiv.innerHTML = '이메일 주소가 정확한지 확인해 주세요.';
			hasClick = true;
			return;
		}

		form.submit();
	}

	function validCheckEmail(e) {
		if (hasClick) {
			const changeEmail = e.target.value.trim();
			const emailDiv = document.querySelector('div#email-check');

			const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

			if (!regExp.test(changeEmail)) {
				emailDiv.innerHTML = '이메일 주소가 정확한지 확인해 주세요.';
			} else {
				emailDiv.innerHTML = '';
			}
		}
	}

});