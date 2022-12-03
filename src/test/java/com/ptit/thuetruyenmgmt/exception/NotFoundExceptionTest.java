package com.ptit.thuetruyenmgmt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class NotFoundExceptionTest extends RuntimeException {

	@Test
	public void testResourceNameArgsConstructor() {
		String name = "TEST NAME";
		NotFoundException thrown = assertThrows(
				NotFoundException.class,
				() -> {
					throw new NotFoundException(name);
				}
		);
		assertEquals(name + " not found!!!", thrown.getMessage());
	}

}
