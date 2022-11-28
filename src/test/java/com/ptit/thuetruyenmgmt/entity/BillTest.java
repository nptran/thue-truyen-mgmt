package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.Bill;
import static org.junit.jupiter.api.Assertions.*;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.Staff;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BillTest {

//    private Bill bill;

    @Test
    void testSetters() throws NoSuchFieldException, IllegalAccessException {
        // GIVEN
        final Bill bill = new Bill();
        Integer expectedId = 1;
        double expectedTotalAmount = 100;
        LocalDateTime expectedCreateTime = LocalDateTime.now();
        List<RentedBook> expectedRentedBooks = new ArrayList<>();
        Staff expectedStaff = new Staff();

        // WHEN
        bill.setId(expectedId);
        bill.setTotalAmount(expectedTotalAmount);
        bill.setCreateTime(expectedCreateTime);
        bill.setRentedBooks(expectedRentedBooks);
        bill.setStaff(expectedStaff);

        // THEN
        final Field id = bill.getClass().getDeclaredField("id");
        final Field totalAmount = bill.getClass().getDeclaredField("totalAmount");
        final Field createTime = bill.getClass().getDeclaredField("createTime");
        final Field rentedBooks = bill.getClass().getDeclaredField("rentedBooks");
        final Field staff = bill.getClass().getDeclaredField("staff");
        // Allow accessible on private fields
        id.setAccessible(true);
        totalAmount.setAccessible(true);
        createTime.setAccessible(true);
        rentedBooks.setAccessible(true);
        staff.setAccessible(true);
        assertEquals(expectedId, id.get(bill));
        assertEquals(expectedTotalAmount, totalAmount.get(bill));
        assertEquals(expectedCreateTime, createTime.get(bill));
        assertEquals(expectedRentedBooks, rentedBooks.get(bill));
        assertEquals(expectedStaff, staff.get(bill));
    }

    @Test
    void testGetters() throws NoSuchFieldException, IllegalAccessException {
        // Given
        final Bill bill = new Bill();
        final Field id = bill.getClass().getDeclaredField("id");
        final Field totalAmount = bill.getClass().getDeclaredField("totalAmount");
        final Field createTime = bill.getClass().getDeclaredField("createTime");
        final Field rentedBooks = bill.getClass().getDeclaredField("rentedBooks");
        final Field staff = bill.getClass().getDeclaredField("staff");
        // Allow accessible on private fields
        id.setAccessible(true);
        totalAmount.setAccessible(true);
        createTime.setAccessible(true);
        rentedBooks.setAccessible(true);
        staff.setAccessible(true);


        Integer expectedId = 1;
        double expectedTotalAmount = 100;
        LocalDateTime expectedCreateTime = LocalDateTime.now();
        List<RentedBook> expectedRentedBooks = new ArrayList<>();
        Staff expectedStaff = new Staff();

        id.set(bill, expectedId);
        totalAmount.set(bill, expectedTotalAmount);
        createTime.set(bill, expectedCreateTime);
        rentedBooks.set(bill, expectedRentedBooks);
        staff.set(bill, expectedStaff);

        // When
        //TODO: https://stackoverflow.com/questions/21354311/junit-test-of-setters-and-getters-of-instance-variables
        Integer actualId = id.getValue();
        double actualTotalAmount = 100;
        LocalDateTime actualCreateTime = LocalDateTime.now();
        List<RentedBook> actualRentedBooks = new ArrayList<>();
        Staff actualStaff = new Staff();

        // Then
        assertEquals(expectedId, id.get(bill));
        assertEquals(expectedTotalAmount, totalAmount.get(bill));
        assertEquals(expectedCreateTime, createTime.get(bill));
        assertEquals(expectedRentedBooks, rentedBooks.get(bill));
        assertEquals(expectedStaff, staff.get(bill));
    }

}
