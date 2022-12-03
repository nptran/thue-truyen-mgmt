package com.ptit.thuetruyenmgmt.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FailedToPayExceptionTest {

    @Test
    public void testNoArgsConstructor() throws FailedToPayException {
        FailedToPayException thrown = assertThrows(
                FailedToPayException.class, // Expected Type
                () -> {throw new FailedToPayException();} // WHEN
        );

        assertEquals("Thanh toán thất bại!!!", thrown.getMessage());
    }

    @Test
    public void testMsgArgsConstructor() throws FailedToPayException {
        String msg = "TEST MESSAGE";
        FailedToPayException thrown = assertThrows(
                FailedToPayException.class, // Expected Type
                () -> {throw new FailedToPayException(msg);} // WHEN
        );

        assertEquals("Thanh toán thất bại!!!\n" + msg, thrown.getMessage()); // Expected message
    }

}
