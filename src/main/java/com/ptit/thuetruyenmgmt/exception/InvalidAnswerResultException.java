package com.ptit.thuetruyenmgmt.exception;

/**
 * Bắn ra khi có nhiều hơn số câu trả lời đúng được thêm vào cho một câu hỏi
 * only choice.
 *
 * @author tnphuc
 *
 */
@SuppressWarnings("serial")
public class InvalidAnswerResultException extends RuntimeException {

	public InvalidAnswerResultException() {
		super("Your question is getting more correct answers than specified. Add another Question and enable `hasMultiChoice` to true if you want it contains more than one correct answer.");
	}

}
