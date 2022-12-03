package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class RentedBookPenaltyTest implements EntityTest {

    private RentedBookPenalty rentedBookPenalty;

    private final RentedBookPenaltyKey expectedId = RentedBookPenaltyKey.builder().rentedBookId(1).penaltyId(1).build();
    private final RentedBook expectedRentedBook = RentedBook.builder().id(1).build();
    private final Penalty expectedPenalty = Penalty.builder().id(1).build();
    private final double expectedFee = 1200.99;

    private Field id;
    private Field rentedBook;
    private Field penalty;
    private Field fee;

    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        rentedBookPenalty = new RentedBookPenalty();

        id = rentedBookPenalty.getClass().getDeclaredField("id");
        rentedBook = rentedBookPenalty.getClass().getDeclaredField("rentedBook");
        penalty = rentedBookPenalty.getClass().getDeclaredField("penalty");
        fee = rentedBookPenalty.getClass().getDeclaredField("fee");

        id.setAccessible(true);
        rentedBook.setAccessible(true);
        penalty.setAccessible(true);
        fee.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        rentedBookPenalty = new RentedBookPenalty(
                expectedId,
                expectedRentedBook,
                expectedPenalty,
                expectedFee
        );

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        rentedBookPenalty = RentedBookPenalty.builder()
                .id(expectedId)
                .rentedBook(expectedRentedBook)
                .penalty(expectedPenalty)
                .fee(expectedFee)
                .build();
        
        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        rentedBookPenalty.setId(expectedId);
        rentedBookPenalty.setRentedBook(expectedRentedBook);
        rentedBookPenalty.setPenalty(expectedPenalty);
        rentedBookPenalty.setFee(expectedFee);
        
        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        id.set(rentedBookPenalty, expectedId);
        rentedBook.set(rentedBookPenalty, expectedRentedBook);
        penalty.set(rentedBookPenalty, expectedPenalty);
        fee.set(rentedBookPenalty, expectedFee);
        
        assertFields(true);
    }

    @Override
    public void testToString() throws IllegalAccessException {
        
    }

    @Test
    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {
        // When its id is NULL
        RentedBookPenalty anotherRentedBookPenalty = new RentedBookPenalty();
        Field anotherId = anotherRentedBookPenalty.getClass().getDeclaredField("id");
        anotherId.setAccessible(true);
        anotherId.set(anotherRentedBookPenalty, expectedId);
        assertFalse(rentedBookPenalty.equals(anotherRentedBookPenalty));

        // Compare to another with id = NULL too
        anotherId.set(anotherRentedBookPenalty, null);
        assertTrue(rentedBookPenalty.equals(anotherRentedBookPenalty));

        /*
         *  WHEN this id != NULL
         */
        id.set(rentedBookPenalty, expectedId);

        // Compare to itself
        RentedBookPenalty cloneRentedBookPenalty = rentedBookPenalty;
        assertTrue(rentedBookPenalty.equals(cloneRentedBookPenalty));

        // Compare to NULL
        assertFalse(rentedBookPenalty.equals(null));

        // Compare to another same id
        anotherId.set(anotherRentedBookPenalty, expectedId);
        assertTrue(rentedBookPenalty.equals(anotherRentedBookPenalty));

        // Compare to another different id
        anotherId.set(anotherRentedBookPenalty, new RentedBookPenaltyKey(404,404));
        assertFalse(rentedBookPenalty.equals(anotherRentedBookPenalty));

        // Compare to another different id = null
        anotherId.set(anotherRentedBookPenalty, null);
        assertFalse(rentedBookPenalty.equals(anotherRentedBookPenalty));

        // Compare to another Type
        Object obj = new Object();
        assertFalse(rentedBookPenalty.equals(obj));
    }

    @Test
    @Override
    public void testHashCode() throws IllegalAccessException {
        int expectedNumber = Objects.hash(expectedId);
        id.set(rentedBookPenalty, expectedId);
        assertEquals(expectedNumber, rentedBookPenalty.hashCode());
    }

    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(rentedBookPenalty));
            assertEquals(expectedRentedBook, rentedBook.get(rentedBookPenalty));
            assertEquals(expectedPenalty, penalty.get(rentedBookPenalty));
            assertEquals(expectedFee, fee.get(rentedBookPenalty));
            return;
        }

        assertEquals(expectedId, rentedBookPenalty.getId());
        assertEquals(expectedRentedBook, rentedBookPenalty.getRentedBook());
        assertEquals(expectedPenalty, rentedBookPenalty.getPenalty());
        assertEquals(expectedFee, rentedBookPenalty.getFee());
    }
}
