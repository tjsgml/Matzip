package com.itwill.matzip.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.MyPickRegisterDto;
import com.itwill.matzip.dto.UpdateRequestItemDto;
import com.itwill.matzip.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rest")
public class RestaurantController {
	private final RestaurantService restSvc;
	
	
	//details.html 보여주기(기본적인 음식점 정보 넣기)
	@GetMapping("/details")
	public String showdetails(@RequestParam(name="id")Long restId,Model model) {
		
		Restaurant rest = restSvc.findOneRest(restId);
		
		model.addAttribute("rest",rest);
		
		log.info(rest.toString());
		
		return "restaurant/details";
	}
	
	//js로 음식점 정보 전달
	@GetMapping("/details/{restId}")
	public ResponseEntity<Restaurant> deliverMap(@PathVariable("restId") Long restId){
		
		Restaurant rest = restSvc.findOneRest(restId);
		
		return ResponseEntity.ok(rest);
	}
	//영업시간 정보 가져오기
	@GetMapping("/details/businessHour/{restId}")
	public ResponseEntity<List<BusinessHour>> findBusinessHour(@PathVariable("restId") Long id){
		log.info("findBusinessHour 호출");
		List<BusinessHour> bsHourList = restSvc.findBsHour(id);
		
		return ResponseEntity.ok(bsHourList);
	}
	//메뉴 정보 가져오기
	@GetMapping("/details/menu/{restId}")
	public ResponseEntity<List<Menu>> findMune(@PathVariable("restId") Long id){
		log.info("@@@@@ findMenu() 호출");
		
		List<Menu> menuList = restSvc.findMenus(id);
		
		log.info("menuList={}",menuList);
		
		return ResponseEntity.ok(menuList);
	}
	
	//좋아요 버튼 클릭시 로그인 상태인지 확인. 로그인 상태면 memberId 반환, 로그아웃 상태면 null을 반환 
	@GetMapping("/details/checkMember")
	@ResponseBody
	public ResponseEntity<Long> checkLogin(){
		//현재 인증된 사용자의 정보를 가져온다.
	     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     
	     log.info("@@@@ 로그인 정보 ={}",auth.toString());
	     

	        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
	            // 사용자가 로그아웃되어 있음을 반환
	            return ResponseEntity.ok().body(null);
	            
	        } else {
	            // 사용자가 로그인되어 있으면 사용자의 이름을 반환
	            String username = auth.getName();
	            
	            Long memberId = restSvc.findMemberId(username);
	            
	            return ResponseEntity.ok().body(memberId);
	        }
	     
	}
	//유저의 좋아요 상태 확인. 
	@GetMapping("/details/checkMyPick")
	
	public ResponseEntity<Long> checkMyPick(@RequestParam("restId") Long restId, @RequestParam("memberId") Long memberId){
		
		log.info("멤버 아이디 ={}, 음식점 아이디={}",memberId,restId);
		
		Long myPickId = restSvc.checkMyPick(memberId,restId);
		
		return ResponseEntity.ok(myPickId);
	}
	//좋아요 삭제
	@DeleteMapping("/details/myPick/{myPickId}")
	public ResponseEntity<String> deleteMyPick (@PathVariable(name = "myPickId") Long myPickId){
		
		restSvc.deleteMyPick(myPickId);
		
		return ResponseEntity.ok("OK");
	}
	//좋아요 행 삽입
	@PostMapping("/details/registerMyPick")
	public ResponseEntity<Long> registerMyPick(@RequestBody MyPickRegisterDto dto){ 
		log.info("@@@ registerMyPick 호출(memberId={},restId={})",dto.getMemberId(),dto.getRestId());
		Long myPickId = restSvc.registerMyPick(dto.getMemberId(),dto.getRestId());
		log.info("@@@@@ 좋아요 클릭시 아이디={}",myPickId);
		return ResponseEntity.ok(myPickId);
	}
	
	//정보 수정 메시지 저장
	@PostMapping("/details/update")
	public ResponseEntity<Integer> updateRequest(@RequestBody UpdateRequestItemDto dto){
		log.info("@@@ updateRequest 호출(restId={},content={})",dto.getRestId(),dto.getContent());
		restSvc.updateRequest(dto);
		return ResponseEntity.ok(1);
	}
}