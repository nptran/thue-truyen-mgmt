package com.ptit.thuetruyenmgmt.exception;

import com.ptit.thuetruyenmgmt.controller.RentedBookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Được bắn ra khi không tìm thấy tài nguyên.
 * 
 * @author tnphuc
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The resource not found!")
public class NotFoundException extends RuntimeException {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);

	public NotFoundException(String resourceName) {
		super(resourceName + " not found!!!");
		LOGGER.error(resourceName + " NOT FOUND!!!");
	}

}
