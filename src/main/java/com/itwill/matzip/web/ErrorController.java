package com.itwill.matzip.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorController{
	
	@ExceptionHandler(Exception.class) // 모든 예외처리
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		HttpStatus status = getStatus(req);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorStatus", status.value());
		mav.addObject("errorMessage", status.getReasonPhrase());
		mav.addObject("errorDetail", e.getMessage());
		
		switch (status.value()) {
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
	    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
	    if (statusCode == null) {
	        return HttpStatus.INTERNAL_SERVER_ERROR;
	    }
	    return HttpStatus.valueOf(statusCode);
	}
	
	
}
