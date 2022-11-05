package com.ptit.thuetruyenmgmt.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author tnphuc
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
	
	private String code;
	
	private String message;
	
}
