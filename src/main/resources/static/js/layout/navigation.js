/**
 *네비게이션의 로그인, 로그아웃 리다이렉트를 위한 js,, 
 */

 document.addEventListener('DOMContentLoaded',() => {
	// 현재 페이지의 URL
    const currentUrl = window.location.href;
    
    const btnLogout = document.getElementById('logoutButton');
    
    const btnLogin = document.getElementById('btnLogin');
    
    
    
 	//로그아웃 버튼 클릭시
    if(btnLogout != null){
		
	 	 btnLogout.addEventListener('click', () => {
	    // 로그아웃 URL
	    const logoutUrl = '/logout?redirect=' + encodeURIComponent(currentUrl);
	    
	    // 로그아웃 요청 보내기
	    window.location.href = logoutUrl;
	    });
	}
    
    //로그인 버튼 클릭시
    if(btnLogin != null){
	    btnLogin.addEventListener('click', () => {
			console.log('로그인 버튼 클릭');
		    // 현재 페이지의 URL을 가져와서 로그인 페이지로 이동하면서 전달
		    window.location.href = `/member/detailLogin?redirect=${currentUrl}`;
		});
	}
	
	const searchInput = document.getElementById('navSearchInput');
	
	searchInput.addEventListener('keydown',(e) => {
		if(e.key ==='Enter'){
			console.log('엔터!');
			const keyword = searchInput.value;
			console.log('데이터 = ',keyword);
			if(keyword == ''){
				alert('검색어를 입력해주세요!!');
			}else{
				//검색 페이지로 키워드와 함께 보내기.
				window.location.href = `/search/all?keyword=${keyword}`;
			}
		}
	});
	
	
 });