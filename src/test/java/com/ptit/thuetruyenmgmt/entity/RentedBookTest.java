package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class RentedBookTest implements EntityTest {

    private RentedBook rentedBook;

    private final Integer expectedId = 1;
    private final double expectedAmount = 100.99;
    private final LocalDateTime expectedRentedTime = LocalDateTime.of(2022, 10, 10, 10, 10, 10);
    private final BookTitle expectedBookTitle = BookTitle.builder().id(1).build();
    private final Customer expectedCustomer = Customer.builder().id(1).build();
    private final Bill expectedBill = Bill.builder().id(1).build();
    private List<RentedBookPenalty> expectedPenalties;
    private final boolean expectedIsPaid = false;

    private Field id;
    private Field amount;
    private Field rentedTime;
    private Field bookTitle;
    private Field customer;
    private Field bill;
    private Field penalties;
    private Field isPaid;

    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        rentedBook = new RentedBook();
        RentedBookPenalty rbp1 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(100, expectedId)).fee(1000).build();
        RentedBookPenalty rbp2 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(101, expectedId)).fee(1000).build();
        RentedBookPenalty rbp3 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(102, expectedId)).fee(1000).build();
        expectedPenalties = new ArrayList<>();
        expectedPenalties.add(rbp1);
        expectedPenalties.add(rbp2);
        expectedPenalties.add(rbp3);

        id = rentedBook.getClass().getDeclaredField("id");
        amount = rentedBook.getClass().getDeclaredField("amount");
        rentedTime = rentedBook.getClass().getDeclaredField("rentedTime");
        bookTitle = rentedBook.getClass().getDeclaredField("bookTitle");
        customer = rentedBook.getClass().getDeclaredField("customer");
        bill = rentedBook.getClass().getDeclaredField("bill");
        penalties = rentedBook.getClass().getDeclaredField("penalties");
        isPaid = rentedBook.getClass().getDeclaredField("isPaid");

        id.setAccessible(true);
        amount.setAccessible(true);
        rentedTime.setAccessible(true);
        bookTitle.setAccessible(true);
        customer.setAccessible(true);
        bill.setAccessible(true);
        penalties.setAccessible(true);
        isPaid.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        rentedBook = new RentedBook(
                expectedId,
                expectedAmount,
                expectedRentedTime,
                expectedBookTitle,
                expectedCustomer,
                expectedBill,
                expectedPenalties,
                expectedIsPaid
        );

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        // WHEN
        rentedBook = RentedBook.builder()
                .id(expectedId)
                .amount(expectedAmount)
                .rentedTime(expectedRentedTime)
                .bookTitle(expectedBookTitle)
                .customer(expectedCustomer)
                .bill(expectedBill)
                .penalties(expectedPenalties)
                .isPaid(expectedIsPaid)
                .build();

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        // WHEN
        rentedBook.setId(expectedId);
        rentedBook.setAmount(expectedAmount);
        rentedBook.setRentedTime(expectedRentedTime);
        rentedBook.setBookTitle(expectedBookTitle);
        rentedBook.setCustomer(expectedCustomer);
        rentedBook.setBill(expectedBill);
        rentedBook.setPenalties(expectedPenalties);
        rentedBook.setPaid(expectedIsPaid);

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(rentedBook, expectedId);
        amount.set(rentedBook, expectedAmount);
        rentedTime.set(rentedBook, expectedRentedTime);
        bookTitle.set(rentedBook, expectedBookTitle);
        customer.set(rentedBook, expectedCustomer);
        bill.set(rentedBook, expectedBill);
        penalties.set(rentedBook, expectedPenalties);
        isPaid.set(rentedBook, expectedIsPaid);

        // THEN
        assertFields(true);
    }

    @Override
    public void testToString() throws IllegalAccessException {
    }

    @Test
    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {
        // When this id is NULL
        RentedBook anotherRentedBook = new RentedBook();
        Field anotherId = anotherRentedBook.getClass().getDeclaredField("id");
        anotherId.setAccessible(true);
        anotherId.set(anotherRentedBook, expectedId);
        assertFalse(rentedBook.equals(anotherRentedBook));

        // Compare to another with id = NULL too
        anotherId.set(anotherRentedBook, null);
        assertTrue(rentedBook.equals(anotherRentedBook));

        /*
         *  WHEN this id != NULL
         */
        id.set(rentedBook, expectedId);

        // Compare to itself
        RentedBook cloneRentedBook = rentedBook;
        assertTrue(rentedBook.equals(cloneRentedBook));

        // Compare to NULL
        assertFalse(rentedBook.equals(null));

        // Compare to another same id
        anotherId.set(anotherRentedBook, expectedId);
        assertTrue(rentedBook.equals(anotherRentedBook));

        // Compare to another different id
        anotherId.set(anotherRentedBook, 404);
        assertFalse(rentedBook.equals(anotherRentedBook));

        // Compare to another different id = null
        anotherId.set(anotherRentedBook, null);
        assertFalse(rentedBook.equals(anotherRentedBook));

        // Compare to another Type
        Object obj = new Object();
        assertFalse(rentedBook.equals(obj));
    }

    @Test
    @Override
    public void testHashCode() {
        int expectedNumber = RentedBook.class.hashCode();
        assertEquals(expectedNumber, rentedBook.hashCode());
    }


    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(rentedBook));
            assertEquals(expectedAmount, amount.get(rentedBook));
            assertEquals(expectedRentedTime, rentedTime.get(rentedBook));
            assertEquals(expectedBookTitle, bookTitle.get(rentedBook));
            assertEquals(expectedCustomer, customer.get(rentedBook));
            assertEquals(expectedBill, bill.get(rentedBook));
            assertEquals(expectedPenalties, penalties.get(rentedBook));
            assertEquals(expectedIsPaid, isPaid.get(rentedBook));
            return;
        }

        assertEquals(expectedId, rentedBook.getId());
        assertEquals(expectedAmount, rentedBook.getAmount());
        assertEquals(expectedRentedTime, rentedBook.getRentedTime());
        assertEquals(expectedBookTitle, rentedBook.getBookTitle());
        assertEquals(expectedCustomer, rentedBook.getCustomer());
        assertEquals(expectedBill, rentedBook.getBill());
        assertEquals(expectedPenalties, rentedBook.getPenalties());
        assertEquals(expectedIsPaid, rentedBook.isPaid());
    }

}
