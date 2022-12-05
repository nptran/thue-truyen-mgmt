package com.ptit.thuetruyenmgmt.entity.dto;

import com.ptit.thuetruyenmgmt.entity.EntityTest;
import com.ptit.thuetruyenmgmt.model.dto.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.dto.ReturnRentedBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReturnRentedBookRequestTest implements EntityTest {

    private ReturnRentedBookRequest returnRentedBookRequest;

    private final int expectedCustomerId = 1;

    private List<RentedBookDTO> expectedRentedBookDtos;

    private Field customerId;

    private Field rentedBookDtos;

    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        returnRentedBookRequest = new ReturnRentedBookRequest();

        expectedRentedBookDtos = new ArrayList<>();
        RentedBookDTO b1 = new RentedBookDTO();
        RentedBookDTO b2 = new RentedBookDTO();
        RentedBookDTO b3 = new RentedBookDTO();
        Field bookId = b1.getClass().getDeclaredField("rentedBookId");
        Field customerIdOfBook = b1.getClass().getDeclaredField("customerId");
        bookId.setAccessible(true);
        customerIdOfBook.setAccessible(true);
        bookId.set(b1, 1);
        customerIdOfBook.set(b1, expectedCustomerId);
        bookId = b2.getClass().getDeclaredField("rentedBookId");
        customerIdOfBook = b2.getClass().getDeclaredField("customerId");
        bookId.setAccessible(true);
        customerIdOfBook.setAccessible(true);
        bookId.set(b2, 2);
        customerIdOfBook.set(b2, expectedCustomerId);
        bookId = b3.getClass().getDeclaredField("rentedBookId");
        customerIdOfBook = b3.getClass().getDeclaredField("customerId");
        bookId.setAccessible(true);
        customerIdOfBook.setAccessible(true);
        bookId.set(b3, 3);
        customerIdOfBook.set(b3, expectedCustomerId);
        expectedRentedBookDtos.add(b1);
        expectedRentedBookDtos.add(b2);
        expectedRentedBookDtos.add(b3);

        customerId = returnRentedBookRequest.getClass().getDeclaredField("customerId");
        rentedBookDtos = returnRentedBookRequest.getClass().getDeclaredField("rentedBookDtos");
        customerId.setAccessible(true);
        rentedBookDtos.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        returnRentedBookRequest = new ReturnRentedBookRequest(
                expectedCustomerId,
                expectedRentedBookDtos
        );

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        returnRentedBookRequest = ReturnRentedBookRequest.builder()
                .customerId(expectedCustomerId)
                .rentedBookDtos(expectedRentedBookDtos)
                .build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        returnRentedBookRequest.setCustomerId(expectedCustomerId);
        returnRentedBookRequest.setRentedBookDtos(expectedRentedBookDtos);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        customerId.set(returnRentedBookRequest, expectedCustomerId);
        rentedBookDtos.set(returnRentedBookRequest, expectedRentedBookDtos);

        assertFields(true);
    }

    @Override
    public void testToString() throws IllegalAccessException {

    }

    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {

    }

    @Override
    public void testHashCode() throws IllegalAccessException {

    }

    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedCustomerId, customerId.get(returnRentedBookRequest));
            assertEquals(expectedRentedBookDtos, rentedBookDtos.get(returnRentedBookRequest));
            return;
        }

        assertEquals(expectedCustomerId, returnRentedBookRequest.getCustomerId());
        assertEquals(expectedRentedBookDtos, returnRentedBookRequest.getRentedBookDtos());
    }
}
