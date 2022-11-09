package com.ptit.thuetruyenmgmt.exception;


public class FailedToResetBookPenaltiesException extends RuntimeException {

    public FailedToResetBookPenaltiesException(String msg) {
        super("Cập nhật lỗi truyện thất bại!!!\n" + msg);
    }

}
