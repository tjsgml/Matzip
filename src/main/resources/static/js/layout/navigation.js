/**
 *네비게이션의 로그인, 로그아웃 리다이렉트를 위한 js,, 
 */

 document.addEventListener('DOMContentLoaded',() => {
	// 현재 페이지의 URL
    const currentUrl = window.location.href;
    //로그아웃 버튼(로그아웃 후 원래 페이지로 돌아가기)
    const btnLogout = document.getElementById('logoutButton');
    //로그인 버튼(로그인 후 원래 페이지로 돌아가기)
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
	const navSearchInputImg = document.getElementById('navSearchInputImg');
	const restaurantTypeSelect = document.getElementById('restaurantTypeSelect');
	//검색(엔터)
	searchInput.addEventListener('keydown',(e) => {
		if(e.key ==='Enter'){
			console.log('엔터!');
			const keyword = searchInput.value;
			console.log('데이터 = ',keyword);
			    
			if(keyword == ''){
				alert('검색어를 입력해주세요!!');
			}else{
				//검색 페이지로 키워드와 함께 보내기.
				const categoryId = restaurantTypeSelect.value;
			    console.log('검색 시 카테고리:',categoryId);
				window.location.href = `/search/all?keyword=${keyword}&categoryId=${categoryId}`;
			}
		}
	});
	//검색(클릭)
	navSearchInputImg.addEventListener('click', () => {
		const keyword = searchInput.value;
		console.log('데이터 = ',keyword);
		if(keyword == ''){
			alert('검색어를 입력해주세요!!');
		}else{
			//검색 페이지로 키워드와 함께 보내기.
			window.location.href = `/search/all?keyword=${keyword}`;
		}
	});
	
	
	
 });