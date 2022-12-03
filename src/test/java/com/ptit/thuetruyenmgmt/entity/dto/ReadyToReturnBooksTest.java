package com.ptit.thuetruyenmgmt.entity.dto;

import com.ptit.thuetruyenmgmt.entity.EntityTest;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.request.ReadyToReturnBooks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReadyToReturnBooksTest implements EntityTest {

    private ReadyToReturnBooks readyToReturnBooks;

    private final Integer expectedCustomerId = 1;

    private List<RentedBook> expectedWillBeReturnedBooks;

    private Field customerId;
    private Field willBeReturnedBooks;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        readyToReturnBooks = new ReadyToReturnBooks();
        expectedWillBeReturnedBooks = new ArrayList<>();
        RentedBook b1 = new RentedBook();
        RentedBook b2 = new RentedBook();
        RentedBook b3 = new RentedBook();
        Field bookId = b1.getClass().getDeclaredField("id");
        bookId.setAccessible(true);
        bookId.set(b1, 1);
        bookId = b2.getClass().getDeclaredField("id");
        bookId.setAccessible(true);
        bookId.set(b2, 2);
        bookId = b3.getClass().getDeclaredField("id");
        bookId.setAccessible(true);
        bookId.set(b3, 3);
        expectedWillBeReturnedBooks.add(b1);
        expectedWillBeReturnedBooks.add(b2);
        expectedWillBeReturnedBooks.add(b3);

        customerId = readyToReturnBooks.getClass().getDeclaredField("customerId");
        willBeReturnedBooks = readyToReturnBooks.getClass().getDeclaredField("willBeReturnedBooks");
        customerId.setAccessible(true);
        willBeReturnedBooks.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        readyToReturnBooks = new ReadyToReturnBooks(expectedWillBeReturnedBooks, expectedCustomerId);

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        readyToReturnBooks = ReadyToReturnBooks.builder()
                .willBeReturnedBooks(expectedWillBeReturnedBooks)
                .customerId(expectedCustomerId)
                .build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        readyToReturnBooks.setWillBeReturnedBooks(expectedWillBeReturnedBooks);
        readyToReturnBooks.setCustomerId(expectedCustomerId);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        willBeReturnedBooks.set(readyToReturnBooks, expectedWillBeReturnedBooks);
        customerId.set(readyToReturnBooks, expectedCustomerId);

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
            assertEquals(expectedCustomerId, customerId.get(readyToReturnBooks));
            assertEquals(expectedWillBeReturnedBooks, willBeReturnedBooks.get(readyToReturnBooks));
            return;
        }

        assertEquals(expectedCustomerId, readyToReturnBooks.getCustomerId());
        assertEquals(expectedWillBeReturnedBooks, readyToReturnBooks.getWillBeReturnedBooks());
    }
}
