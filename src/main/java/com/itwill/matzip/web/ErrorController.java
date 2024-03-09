package com.itwill.matzip.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 처리 가능
public class ErrorController{
	
	@ExceptionHandler(Exception.class) // @ExceptionHandler 특정 예외 유형 처리 메서드 정의  -> Exception.class를 인자로 받아, *모든 종류의 예외*를 처리
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		HttpStatus status = getStatus(req);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorStatus", status.value()); // http상태코드
		mav.addObject("errorMessage", status.getReasonPhrase()); // 상태 메시지
		mav.addObject("errorDetail", e.getMessage()); // 예외 메시지
		
		switch (status.value()) { // 에러 이미지
	        case 404:
	            mav.addObject("errorImage", "/images/errors/404.png");
	            break;
	        case 500:
	            mav.addObject("errorImage", "/images/errors/500.png");
	            break;
	        default:
	            mav.addObject("errorImage", "/images/errors/default.png");
	    }
	
	    mav.setViewName("error/customError");
	    return mav;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
	    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code"); // 상태코드 가져옴
	    if (statusCode == null) { 
	        return HttpStatus.INTERNAL_SERVER_ERROR; // 상태코드 null이면 500으로
	    }
	    return HttpStatus.valueOf(statusCode); // 해당 정수 값에 해당하는 HttpStatus 열거형 상수를 반환
	}
	
	
}
