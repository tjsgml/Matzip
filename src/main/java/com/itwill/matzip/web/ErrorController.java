package com.itwill.matzip.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 처리 가능
public class ErrorController {
	
	@ExceptionHandler(Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		HttpStatus status = getStatus(req);

		ModelAndView mav = new ModelAndView();
		mav.addObject("errorStatus", status.value()); // http 상태코드 
		mav.addObject("errorMessage", status.getReasonPhrase()); // 상태 메시지
		mav.addObject("errorDetail", e.getMessage()); // 예외 메시지

		// ErrorController 내부에 추가
		if (status.is4xxClientError()) {
			mav.addObject("errorImage", "/img/errors.jpg");
			mav.addObject("userMessage", "요청하신 페이지를 찾을 수 없습니다. URL을 확인해주세요. \r\n"
					+ "						문제가 계속되면 홈페이지로 돌아가 다른 정보를 찾아보시는 것을 권장합니다"); // 400번대 에러 메시지
		} else if (status.is5xxServerError()) {
			mav.addObject("errorImage", "/img/errors.jpg"); // "/images/errors/5xx.png"
			mav.addObject("userMessage", "오우! 문제가 있군요! 관리자에게 문의 해주세요!"); // 500번대 에러 메시지
		} else {
			mav.addObject("errorImage", "/img/errors.jpg");
			mav.addObject("userMessage", "알 수 없는 에러가 발생했습니다."); // 기본 에러 메시지
		}

		mav.setViewName("error/customError");
		return mav;
	}


	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		HttpStatus status = HttpStatus.valueOf(statusCode);

		// 400번대 에러 처리
		if (status.is4xxClientError()) {
			return HttpStatus.BAD_REQUEST; // 400번대 에러는 모두 BAD_REQUEST로 처리
		}
		// 500번대 에러 처리
		else if (status.is5xxServerError()) {
			return HttpStatus.INTERNAL_SERVER_ERROR; // 500번대 에러는 모두 INTERNAL_SERVER_ERROR로 처리
		}
		return status;
	}

}
