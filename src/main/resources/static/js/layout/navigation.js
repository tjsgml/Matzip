/**
 *네비게이션의 로그인, 로그아웃 리다이렉트를 위한 js,, 
 */

 document.addEventListener('DOMContentLoaded',() => {
	// 현재 페이지의 URL
    const currentUrl = window.location.href;
    
    const btnLogout = document.getElementById('logoutButton');
    
    const btnLogin = document.getElementById('btnLogin');
    
    if(btnLogout != null){
		
	 	//로그아웃 버튼 클릭시
	 	 btnLogout.addEventListener('click', () => {
	    // 로그아웃 URL
	    const logoutUrl = '/logout?redirect=' + encodeURIComponent(currentUrl);
	    
	    // 로그아웃 요청 보내기
	    window.location.href = logoutUrl;
	    });
	}
    
    if(btnLogin != null){
	    //로그인 버튼 클릭시
	    btnLogin.addEventListener('click', () => {
			console.log('로그인 버튼 클릭');
		    // 현재 페이지의 URL을 가져와서 로그인 페이지로 이동하면서 전달
		    window.location.href = `/member/detailLogin?redirect=${currentUrl}`;
		});
	}
	
	
 });