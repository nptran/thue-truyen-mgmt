package com.ptit.thuetruyenmgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Xử lý phản hồi thay cho các Controller thông thường khi một {@link Exception}
 * bị bắn ra tại Service.
 * 
 * @author tnphuc
 * 
 * @see ErrorResponse
 */
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(AlreadyTakenException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handlerAlreadyTakenException(AlreadyTakenException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(DuplicatedException.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public ErrorResponse handlerDuplicatedException(DuplicatedException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerNotFoundException(NotFoundException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(BadParamException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerNotFoundException(BadParamException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(InvalidAnswerResultException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerInvalidAnswerResultException(InvalidAnswerResultException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// Username không tồn tại
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerUsernameNotFoundException(UsernameNotFoundException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	// Sai mật khẩu
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handlerBadCredentialsException(BadCredentialsException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	// Level câu hỏi chưa được chỉ định
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerInvalidLevelException(HttpMessageNotReadableException ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.BAD_REQUEST, "Level does not exist, check again please!");
	}

	// Xử lý tất cả các exception chưa được khai báo
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handlerException(Exception ex, WebRequest req) {
		ex.printStackTrace();
		return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}
}