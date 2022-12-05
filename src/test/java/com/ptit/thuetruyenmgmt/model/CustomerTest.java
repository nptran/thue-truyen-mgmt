package com.ptit.thuetruyenmgmt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class CustomerTest implements EntityTest {

    private Customer customer;

    private final Integer expectedId = 1;

    private final long expectedCCCD = 123456789;

    private final String expectedPhone = "09876323256";

    private final String expectedEmail = "customer@unittest.com";

    private final LocalDate expectedDob = LocalDate.of(1998, 10, 31);

    private final FullName expectedFullName = new FullName(100, "An", "Trần Văn");

    private final Address expectedAddress = new Address(100, "Trần Phú", "Hà Đông", "Hà Nội");

    private Set<RentedBook> expectedRentedBooks;


    private Field id;
    private Field cccd;
    private Field phone;
    private Field email;
    private Field dob;
    private Field fullName;
    private Field address;
    private Field rentedBooks;

    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        customer = new Customer();

        expectedRentedBooks = new HashSet<>();
        RentedBook book1 = RentedBook.builder()
                .id(1)
                .amount(1000)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(1000).build())
                .customer(Customer.builder().id(200).build())
                .isPaid(false)
                .build();
        RentedBook book2 = RentedBook.builder()
                .id(2)
                .amount(3000)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(2000).build())
                .customer(Customer.builder().id(200).build())
                .bill(Bill.builder().id(1).build())
                .isPaid(true)
                .build();
        expectedRentedBooks.add(book1);
        expectedRentedBooks.add(book2);

        id = customer.getClass().getDeclaredField("id");
        cccd = customer.getClass().getDeclaredField("cccd");
        phone = customer.getClass().getDeclaredField("phone");
        email = customer.getClass().getDeclaredField("email");
        dob = customer.getClass().getDeclaredField("dob");
        fullName = customer.getClass().getDeclaredField("fullName");
        address = customer.getClass().getDeclaredField("address");
        rentedBooks = customer.getClass().getDeclaredField("rentedBooks");
        id.setAccessible(true);
        cccd.setAccessible(true);
        phone.setAccessible(true);
        email.setAccessible(true);
        dob.setAccessible(true);
        fullName.setAccessible(true);
        address.setAccessible(true);
        rentedBooks.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        customer =
                new Customer(
                        expectedId,
                        expectedCCCD,
                        expectedPhone,
                        expectedEmail,
                        expectedDob,
                        expectedFullName,
                        expectedAddress,
                        expectedRentedBooks);
        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        // WHEN
        customer = Customer.builder()
                .id(expectedId)
                .cccd(expectedCCCD)
                .phone(expectedPhone)
                .email(expectedEmail)
                .dob(expectedDob)
                .fullName(expectedFullName)
                .address(expectedAddress)
                .rentedBooks(expectedRentedBooks)
                .build();

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        // WHEN
        customer.setId(expectedId);
        customer.setCccd(expectedCCCD);
        customer.setPhone(expectedPhone);
        customer.setEmail(expectedEmail);
        customer.setDob(expectedDob);
        customer.setFullName(expectedFullName);
        customer.setAddress(expectedAddress);
        customer.setRentedBooks(expectedRentedBooks);

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(customer, expectedId);
        cccd.set(customer, expectedCCCD);
        phone.set(customer, expectedPhone);
        email.set(customer, expectedEmail);
        dob.set(customer, expectedDob);
        fullName.set(customer, expectedFullName);
        address.set(customer, expectedAddress);
        rentedBooks.set(customer, expectedRentedBooks);

        // THEN
        assertFields(true);
    }

    @Override
    public void testToString() {

    }

    @Override
    public void testEquals() {

    }

    @Override
    public void testHashCode() {

    }

    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(customer));
            assertEquals(expectedCCCD, cccd.get(customer));
            assertEquals(expectedPhone, phone.get(customer));
            assertEquals(expectedEmail, email.get(customer));
            assertEquals(expectedDob, dob.get(customer));
            assertEquals(expectedFullName, fullName.get(customer));
            assertEquals(expectedAddress, address.get(customer));
            assertEquals(expectedRentedBooks, rentedBooks.get(customer));
            return;
        }

        assertEquals(expectedId, customer.getId());
        assertEquals(expectedCCCD, customer.getCccd());
        assertEquals(expectedPhone, customer.getPhone());
        assertEquals(expectedEmail, customer.getEmail());
        assertEquals(expectedDob, customer.getDob());
        assertEquals(expectedFullName, customer.getFullName());
        assertEquals(expectedAddress, customer.getAddress());
        assertEquals(expectedRentedBooks, customer.getRentedBooks());
    }

}
