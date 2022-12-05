package com.ptit.thuetruyenmgmt.model.key;

import com.ptit.thuetruyenmgmt.model.EntityTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RentedBookPenaltyKeyTest implements EntityTest {

    private RentedBookPenaltyKey key;

    private final Integer expectedRentedBookId = 1;

    private final Integer expectedPenaltyId = 1;

    private Field rentedBookId;
    private Field penaltyId;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        key = new RentedBookPenaltyKey();

        rentedBookId = key.getClass().getDeclaredField("rentedBookId");
        penaltyId = key.getClass().getDeclaredField("penaltyId");
        rentedBookId.setAccessible(true);
        penaltyId.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        key = new RentedBookPenaltyKey(expectedRentedBookId, expectedPenaltyId);

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        key = RentedBookPenaltyKey.builder().rentedBookId(expectedRentedBookId).penaltyId(expectedPenaltyId).build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        key.setRentedBookId(expectedRentedBookId);
        key.setPenaltyId(expectedPenaltyId);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        rentedBookId.set(key, expectedRentedBookId);
        penaltyId.set(key, expectedPenaltyId);

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
            assertEquals(expectedRentedBookId, rentedBookId.get(key));
            assertEquals(expectedPenaltyId, penaltyId.get(key));
            return;
        }

        assertEquals(expectedRentedBookId, key.getRentedBookId());
        assertEquals(expectedPenaltyId, key.getPenaltyId());
    }

}
