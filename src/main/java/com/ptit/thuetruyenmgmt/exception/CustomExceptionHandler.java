package com.ptit.thuetruyenmgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Xử lý phản hồi thay cho các Controller thông thường khi một {@link Exception}
 * bị bắn ra tại Service.
 * 
 * @author tnphuc
 * 
 * @see ErrorResponse
 */
@Deprecated
//@ControllerAdvice
public class CustomExceptionHandler {

//	@ExceptionHandler(Throwable.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(final Throwable throwable) {
//		logger.error("Exception during execution of SpringSecurity application", throwable);
		String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
//		model.addAttribute("errorMessage", errorMessage);
		return "";
	}
}