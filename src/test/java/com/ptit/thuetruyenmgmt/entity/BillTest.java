package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BillTest {

    private Bill bill;

    private Integer expectedId;

    private double expectedTotalAmount;

    private LocalDateTime expectedCreateTime;

    private List<RentedBook> expectedRentedBooks;

    private Staff expectedStaff;

    private Field id;
    private Field totalAmount;
    private Field createTime;
    private Field rentedBooks;
    private Field staff;

    /**
     * GIVEN
     */
    @BeforeEach
    void initData() throws NoSuchFieldException {
        bill = new Bill();
        expectedId = 1;
        expectedTotalAmount = 100.99;
        expectedCreateTime = LocalDateTime.of(2022, 10, 10, 10, 0, 0);

        expectedRentedBooks = new ArrayList<>();
        RentedBook book1 = RentedBook.builder()
                .id(1)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(1000).build())
                .customer(Customer.builder().id(200).build())
                .bill(bill)
                .isPaid(true)
                .build();
        RentedBook book2 = RentedBook.builder()
                .id(2)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(2000).build())
                .customer(Customer.builder().id(200).build())
                .bill(bill)
                .isPaid(true)
                .build();
        expectedRentedBooks.add(book1);
        expectedRentedBooks.add(book2);

        expectedStaff = Staff.staffBuilder().id(10).fullName(new FullName(1, "An", "Phạm")).build();

        id = bill.getClass().getDeclaredField("id");
        totalAmount = bill.getClass().getDeclaredField("totalAmount");
        createTime = bill.getClass().getDeclaredField("createTime");
        rentedBooks = bill.getClass().getDeclaredField("rentedBooks");
        staff = bill.getClass().getDeclaredField("staff");
        // Allow accessible on private fields
        id.setAccessible(true);
        totalAmount.setAccessible(true);
        createTime.setAccessible(true);
        rentedBooks.setAccessible(true);
        staff.setAccessible(true);
    }


    @Test
    void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        bill = new Bill(expectedId, expectedTotalAmount, expectedCreateTime, expectedRentedBooks, expectedStaff);

        // THEN
        assertEquals(expectedId, id.get(bill));
        assertEquals(expectedTotalAmount, totalAmount.get(bill));
        assertEquals(expectedCreateTime, createTime.get(bill));
        assertEquals(expectedRentedBooks, rentedBooks.get(bill));
        assertEquals(expectedStaff, staff.get(bill));
    }


    @Test
    void testBuilder() throws IllegalAccessException {
        // WHEN
        bill = Bill.builder()
                .id(expectedId)
                .totalAmount(expectedTotalAmount)
                .createTime(expectedCreateTime)
                .rentedBooks(expectedRentedBooks)
                .staff(expectedStaff)
                .build();

        // THEN
        assertEquals(expectedId, id.get(bill));
        assertEquals(expectedTotalAmount, totalAmount.get(bill));
        assertEquals(expectedCreateTime, createTime.get(bill));
        assertEquals(expectedRentedBooks, rentedBooks.get(bill));
        assertEquals(expectedStaff, staff.get(bill));
    }


    @Test
    void testSetters() throws IllegalAccessException {
        // WHEN
        bill.setId(expectedId);
        bill.setTotalAmount(expectedTotalAmount);
        bill.setCreateTime(expectedCreateTime);
        bill.setRentedBooks(expectedRentedBooks);
        bill.setStaff(expectedStaff);

        // THEN
        assertEquals(expectedId, id.get(bill));
        assertEquals(expectedTotalAmount, totalAmount.get(bill));
        assertEquals(expectedCreateTime, createTime.get(bill));
        assertEquals(expectedRentedBooks, rentedBooks.get(bill));
        assertEquals(expectedStaff, staff.get(bill));
    }


    @Test
    void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(bill, expectedId);
        totalAmount.set(bill, expectedTotalAmount);
        createTime.set(bill, expectedCreateTime);
        rentedBooks.set(bill, expectedRentedBooks);
        staff.set(bill, expectedStaff);

        // THEN
        assertEquals(expectedId, bill.getId());
        assertEquals(expectedTotalAmount, bill.getTotalAmount());
        assertEquals(expectedCreateTime, bill.getCreateTime());
        assertEquals(expectedRentedBooks, bill.getRentedBooks());
        assertEquals(expectedStaff, bill.getStaff());
    }


}
