package com.ptit.thuetruyenmgmt.exception;

/**
 * Được bắn ra khi một tài nguyên nào đó đã tồn tại trên hệ thống và không thể thêm
 * một tài nguyên mới. <b>Khác với {@link AlreadyTakenException} chỉ kiểm tra
 * với một phần của tài nguyên</b> thì Exception này để đảm bảo tính độc nhất
 * của tài nguyên thông qua các giá trị khóa của chúng.
 *
 * @author tnphuc
 *
 */
@SuppressWarnings("serial")
public class DuplicatedException extends RuntimeException {

	public DuplicatedException(String resourseName) {
		super("This " + resourseName + " has already been existed. Please pick another one!");
	}

}