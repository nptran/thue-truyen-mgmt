package com.ptit.thuetruyenmgmt.exception;

import com.ptit.thuetruyenmgmt.controller.RentedBookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Nothing has been selected to return.")
public class NoneSelectedBookToReturnException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);


    public NoneSelectedBookToReturnException() {
        super("Chưa có Đầu truyện nào được chọn để trả!!!");
        LOGGER.error("NONE SELECTED BOOKS!!!");
    };

}
