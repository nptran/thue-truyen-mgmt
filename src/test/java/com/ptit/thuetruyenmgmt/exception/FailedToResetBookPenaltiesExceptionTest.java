package com.ptit.thuetruyenmgmt.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FailedToResetBookPenaltiesExceptionTest {

    @Test
    public void testMsgArgsConstructor() {
        String msg = "TEST MESSAGE";
        FailedToResetBookPenaltiesException thrown = assertThrows(
                FailedToResetBookPenaltiesException.class,
                () -> {
                    throw new FailedToResetBookPenaltiesException(msg);
                }
        );
        assertEquals("Cập nhật lỗi truyện thất bại!!!\n" + msg, thrown.getMessage());
    }

}
