package com.ptit.thuetruyenmgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "To show an example of a custom message")
public class EmptyResultException extends RuntimeException {

    public EmptyResultException(String resourceName) {
        super("Hiện không có " + resourceName + " nào được tìm thấy");
    }

}
