package com.ptit.thuetruyenmgmt.exception;

/**
 * Được bắn ra khi không tìm thấy tài nguyên.
 * 
 * @author tnphuc
 *
 */
@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

	public NotFoundException(String resourceName) {
		super(resourceName + " not found.");
	}

}
