package com.ptit.thuetruyenmgmt.exception;

import java.util.StringJoiner;

/**
 *
 * @author tnphuc
 *
 */
@SuppressWarnings("serial")
public class BadParamException extends RuntimeException {

	public BadParamException(String... paramNames) {
		super("Parameters you provided are not correct. Check one of the following parameters: " + paramToString(paramNames));
	}

	private static String paramToString(String... paramNames) {
		StringJoiner joiner = new StringJoiner(", ", "", ".");
		for (String name : paramNames) {
			joiner.add(name);
		}
		return joiner.toString();
	}

}