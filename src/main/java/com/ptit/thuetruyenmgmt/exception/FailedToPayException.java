package com.ptit.thuetruyenmgmt.exception;

@SuppressWarnings("serial")
public class FailedToPayException extends RuntimeException {

    public FailedToPayException() {
        super("Thanh toán thất bại!!!");
    }

    public FailedToPayException(String msg) {
        super("Thanh toán thất bại!!!\n" + msg);
    }

}
