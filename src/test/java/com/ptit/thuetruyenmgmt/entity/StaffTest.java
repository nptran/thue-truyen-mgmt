package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
public class StaffTest implements EntityTest {

    private Staff staff;

    private final Integer expectedId = 1;
    private final String expectedLoginCode = "GYTWH8F9D";
    private final LocalDate expectedDob = LocalDate.of(2000, 10, 31);
    private final String expectedEmail = "staff@unittest.com";
    private final String expectedPhone = "09876543345";
    private FullName expectedFullName;
    private Address expectedAddress;
    private List<Bill> expectedBills;
    private final StaffPosition expectedPosition = StaffPosition.THU_NGAN;

    private Field id;
    private Field loginCode;
    private Field dob;
    private Field email;
    private Field phone;
    private Field fullName;
    private Field address;
    private Field bills;
    private Field position;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        staff = new Staff();

        expectedFullName = new FullName();
        Field nameId = expectedFullName.getClass().getDeclaredField("id");
        Field fName = expectedFullName.getClass().getDeclaredField("fName");
        Field lName = expectedFullName.getClass().getDeclaredField("lName");
        nameId.setAccessible(true);
        fName.setAccessible(true);
        lName.setAccessible(true);
        nameId.set(expectedFullName, 1);
        fName.set(expectedFullName, "Thảo");
        lName.set(expectedFullName, "Phạm Thanh");

        expectedAddress = new Address();
        Field addressId = expectedAddress.getClass().getDeclaredField("id");
        Field street = expectedAddress.getClass().getDeclaredField("street");
        Field district = expectedAddress.getClass().getDeclaredField("district");
        Field city = expectedAddress.getClass().getDeclaredField("city");
        addressId.setAccessible(true);
        street.setAccessible(true);
        district.setAccessible(true);
        city.setAccessible(true);
        addressId.set(expectedAddress, 1);
        street.set(expectedAddress, "Trần Phú");
        district.set(expectedAddress, "Hà Đông");
        city.set(expectedAddress, "Hà Nội");

        Bill bill1 = Bill.builder().id(1).staff(staff).build();
        Bill bill2 = Bill.builder().id(2).staff(staff).build();
        Bill bill3 = Bill.builder().id(3).staff(staff).build();
        Bill bill4 = Bill.builder().id(4).staff(staff).build();
        expectedBills = new ArrayList<>();
        expectedBills.add(bill1);
        expectedBills.add(bill2);
        expectedBills.add(bill3);
        expectedBills.add(bill4);

        id = staff.getClass().getSuperclass().getDeclaredField("id");
        loginCode = staff.getClass().getSuperclass().getDeclaredField("loginCode");
        dob = staff.getClass().getSuperclass().getDeclaredField("dob");
        email = staff.getClass().getSuperclass().getDeclaredField("email");
        phone = staff.getClass().getSuperclass().getDeclaredField("phone");
        fullName = staff.getClass().getSuperclass().getDeclaredField("fullName");
        address = staff.getClass().getSuperclass().getDeclaredField("address");
        position = staff.getClass().getDeclaredField("position");
        bills = staff.getClass().getDeclaredField("bills");

        id.setAccessible(true);
        loginCode.setAccessible(true);
        dob.setAccessible(true);
        email.setAccessible(true);
        phone.setAccessible(true);
        fullName.setAccessible(true);
        address.setAccessible(true);
        position.setAccessible(true);
        bills.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        staff = new Staff(
                expectedId,
                expectedLoginCode,
                expectedDob,
                expectedEmail,
                expectedPhone,
                expectedFullName,
                expectedAddress,
                expectedPosition
        );

        assertFields(false, true);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        staff = Staff.staffBuilder()
                .id(expectedId)
                .loginCode(expectedLoginCode)
                .dob(expectedDob)
                .email(expectedEmail)
                .phone(expectedPhone)
                .fullName(expectedFullName)
                .address(expectedAddress)
                .position(expectedPosition)
                .build();

        assertFields(false, true);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        staff.setId(expectedId);
        staff.setLoginCode(expectedLoginCode);
        staff.setDob(expectedDob);
        staff.setEmail(expectedEmail);
        staff.setPhone(expectedPhone);
        staff.setFullName(expectedFullName);
        staff.setAddress(expectedAddress);
        staff.setPosition(expectedPosition);
        staff.setBills(expectedBills);

        assertFields(false, false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        id.set(staff, expectedId);
        loginCode.set(staff, expectedLoginCode);
        dob.set(staff, expectedDob);
        email.set(staff, expectedEmail);
        phone.set(staff, expectedPhone);
        fullName.set(staff, expectedFullName);
        address.set(staff, expectedAddress);
        position.set(staff, expectedPosition);
        bills.set(staff, expectedBills);

        assertFields(true, false);
    }

    @Override
    public void testToString() throws IllegalAccessException {

    }

    @Test
    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {
        // When its id is NULL
        Staff anotherStaff = new Staff();
        Field anotherId = anotherStaff.getClass().getSuperclass().getDeclaredField("id");
        anotherId.setAccessible(true);
        anotherId.set(anotherStaff, expectedId);
        assertFalse(staff.equals(anotherStaff));

        id.set(staff, expectedId);

        // Compare to itself
        Staff cloneStaff = staff;
        assertTrue(staff.equals(cloneStaff));

        // Compare to NULL
        assertFalse(staff.equals(null));

        // Compare to another same id
        anotherId.set(anotherStaff, expectedId);
        assertTrue(staff.equals(anotherStaff));

        // Compare to another different id
        anotherId.set(anotherStaff, 404);
        assertFalse(staff.equals(anotherStaff));

        // Compare to another different id = null
        anotherId.set(anotherStaff, null);
        assertFalse(staff.equals(anotherStaff));

        // Compare to another Type
        Object obj = new Object();
        assertFalse(staff.equals(obj));
    }

    @Test
    @Override
    public void testHashCode() throws IllegalAccessException {
        int expectedNumber = Staff.class.hashCode();
        assertEquals(expectedNumber, staff.hashCode());
    }


    private void assertFields(boolean useGetter, boolean withoutBills) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(staff));
            assertEquals(expectedLoginCode, loginCode.get(staff));
            assertEquals(expectedDob, dob.get(staff));
            assertEquals(expectedEmail, email.get(staff));
            assertEquals(expectedPhone, phone.get(staff));
            assertEquals(expectedFullName, fullName.get(staff));
            assertEquals(expectedAddress, address.get(staff));
            assertEquals(expectedPosition, position.get(staff));
            if (!withoutBills)
                assertEquals(expectedBills, bills.get(staff));
            return;
        }

        assertEquals(expectedId, staff.getId());
        assertEquals(expectedLoginCode, staff.getLoginCode());
        assertEquals(expectedDob, staff.getDob());
        assertEquals(expectedEmail, staff.getEmail());
        assertEquals(expectedPhone, staff.getPhone());
        assertEquals(expectedFullName, staff.getFullName());
        assertEquals(expectedAddress, staff.getAddress());
        assertEquals(expectedPosition, staff.getPosition());
        if (!withoutBills)
            assertEquals(expectedBills, staff.getBills());
    }

}
