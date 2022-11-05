package com.ptit.thuetruyenmgmt.exception;


@SuppressWarnings("serial")
public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Tên nhập hoặc mật khẩu không đúng.");
    }

}