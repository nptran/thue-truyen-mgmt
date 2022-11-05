package com.ptit.thuetruyenmgmt.model.response;

import lombok.*;

/**
 * The common response for the APIs in usual cases.
 *
 * @author tnphuc
 *
 * @param <T>  the Type of data in the response. Usually a DTO object.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class NormalResponse<T> extends BaseResponse {

	private T data;

	@Builder(builderMethodName = "normalResponseBuilder")
	public NormalResponse(String code, String message, T data) {
		super(code, message);
		this.data = data;
	}

}
