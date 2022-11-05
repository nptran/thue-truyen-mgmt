package com.ptit.thuetruyenmgmt.exception;

/**
 * Được bắn ra khi một phần tài nguyên nào đó đã tồn tại trên hệ
 * thống và không thể thêm một tài nguyên mới.
 *
 * @author tnphuc
 *
 */
@SuppressWarnings("serial")
public class AlreadyTakenException extends RuntimeException {

	public AlreadyTakenException(String partName) {
		super(partName + " has already been taken. Please pick another one!");
	}
	
}