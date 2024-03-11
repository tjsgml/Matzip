package com.itwill.matzip.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 처리 가능
public class ErrorController {
	
	@ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(HttpServletRequest req, NoHandlerFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorStatus", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorMessage", "페이지를 찾을 수 없음");
        mav.addObject("errorDetail", "요청하신 페이지를 찾을 수 없습니다. URL을 확인해 주세요.");
        mav.addObject("errorImage", "/img/errors_404.png"); // 경로 오타 수정
        mav.addObject("userMessage", "요청하신 페이지를 찾을 수 없습니다. URL을 확인하거나 홈페이지로 돌아가 다른 정보를 찾아보세요.");
        mav.setViewName("error/customError");
        return mav;
    }

	@ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorStatus", HttpStatus.FORBIDDEN.value());
        mav.addObject("errorMessage", "접근 거부");
        mav.addObject("errorDetail", "페이지에 대한 접근 권한이 없습니다.");
        mav.addObject("errorImage", "/img/errors_403.png");
        mav.addObject("userMessage", "접근이 거부되었습니다. 접근 권한을 확인해주세요.");
        mav.setViewName("error/customError");
        return mav;
    }
	
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorMessage", "서버 내부 오류");
        mav.addObject("errorDetail", "서버에서 처리 중 예상치 못한 오류가 발생했습니다.");
        mav.addObject("errorImage", "/img/errors_403.png");
        mav.addObject("userMessage", "오류가 발생했습니다. 문제가 지속되면 관리자에게 문의해주세요.");
        mav.setViewName("error/customError");
        return mav;
    }

    
}
