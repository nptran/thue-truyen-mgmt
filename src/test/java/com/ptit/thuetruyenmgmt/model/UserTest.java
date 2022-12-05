package com.ptit.thuetruyenmgmt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest implements EntityTest {
    
    private User user;

    private final Integer expectedId = 1;
    private final String expectedLoginCode = "GYTWH8F9D";
    private final LocalDate expectedDob = LocalDate.of(2000, 10, 31);
    private final String expectedEmail = "user@unittest.com";
    private final String expectedPhone = "09876543345";
    private FullName expectedFullName;
    private Address expectedAddress;

    private Field id;
    private Field loginCode;
    private Field dob;
    private Field email;
    private Field phone;
    private Field fullName;
    private Field address;
    
    
    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        user = new User();

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

        id = user.getClass().getDeclaredField("id");
        loginCode = user.getClass().getDeclaredField("loginCode");
        dob = user.getClass().getDeclaredField("dob");
        email = user.getClass().getDeclaredField("email");
        phone = user.getClass().getDeclaredField("phone");
        fullName = user.getClass().getDeclaredField("fullName");
        address = user.getClass().getDeclaredField("address");

        id.setAccessible(true);
        loginCode.setAccessible(true);
        dob.setAccessible(true);
        email.setAccessible(true);
        phone.setAccessible(true);
        fullName.setAccessible(true);
        address.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        user = new User(
                expectedId,
                expectedLoginCode,
                expectedDob,
                expectedEmail,
                expectedPhone,
                expectedFullName,
                expectedAddress
        );
        
        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        user = User.builder()
                .id(expectedId)
                .loginCode(expectedLoginCode)
                .dob(expectedDob)
                .email(expectedEmail)
                .phone(expectedPhone)
                .fullName(expectedFullName)
                .address(expectedAddress)
                .build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        user.setId(expectedId);
        user.setLoginCode(expectedLoginCode);
        user.setDob(expectedDob);
        user.setEmail(expectedEmail);
        user.setPhone(expectedPhone);
        user.setFullName(expectedFullName);
        user.setAddress(expectedAddress);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        id.set(user, expectedId);
        loginCode.set(user, expectedLoginCode);
        dob.set(user, expectedDob);
        email.set(user, expectedEmail);
        phone.set(user, expectedPhone);
        fullName.set(user, expectedFullName);
        address.set(user, expectedAddress);

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
            assertEquals(expectedId, id.get(user));
            assertEquals(expectedLoginCode, loginCode.get(user));
            assertEquals(expectedDob, dob.get(user));
            assertEquals(expectedEmail, email.get(user));
            assertEquals(expectedPhone, phone.get(user));
            assertEquals(expectedFullName, fullName.get(user));
            assertEquals(expectedAddress, address.get(user));
            return;
        }

        assertEquals(expectedId, user.getId());
        assertEquals(expectedLoginCode, user.getLoginCode());
        assertEquals(expectedDob, user.getDob());
        assertEquals(expectedEmail, user.getEmail());
        assertEquals(expectedPhone, user.getPhone());
        assertEquals(expectedFullName, user.getFullName());
        assertEquals(expectedAddress, user.getAddress());
    }
    
}
