package com.ptit.thuetruyenmgmt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class NoneSelectedBookToReturnExceptionTest {

    @Test
    public void testNoArgsConstructor() {
        NoneSelectedBookToReturnException thrown = assertThrows(
                NoneSelectedBookToReturnException.class,
                () -> {
                    throw new NoneSelectedBookToReturnException();
                }
        );
        assertEquals("Chưa có Đầu truyện nào được chọn để trả!!!", thrown.getMessage());
    }

}
