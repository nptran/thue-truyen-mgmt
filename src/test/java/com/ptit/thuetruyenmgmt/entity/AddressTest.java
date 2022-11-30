package com.ptit.thuetruyenmgmt.entity;

import com.ptit.thuetruyenmgmt.model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;


@SpringBootTest
class AddressTest {

    private Address address;

    private final Integer expectedId = 1;

    private final String expectedStreet = "Trần Phú";

    private final String expectedDistrict = "Hà Đông";

    private final String expectedCity = "Hà Nội";

    private Field id;
    private Field street;
    private Field district;
    private Field city;


    /**
     * GIVEN
     */
    @BeforeEach
    void initData() throws NoSuchFieldException {
        address = new Address();

        id = address.getClass().getDeclaredField("id");
        street = address.getClass().getDeclaredField("street");
        district = address.getClass().getDeclaredField("district");
        city = address.getClass().getDeclaredField("city");
        // Allow accessible on private fields
        id.setAccessible(true);
        street.setAccessible(true);
        district.setAccessible(true);
        city.setAccessible(true);
    }


    @Test
    void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        address = new Address(expectedId, expectedStreet, expectedDistrict, expectedCity);

        // THEN
        assertEquals(expectedId, id.get(address));
        assertEquals(expectedStreet, street.get(address));
        assertEquals(expectedDistrict, district.get(address));
        assertEquals(expectedCity, city.get(address));
    }


    @Test
    void testBuilder() throws IllegalAccessException {
        // WHEN
        address = Address.builder()
                .id(expectedId)
                .street(expectedStreet)
                .district(expectedDistrict)
                .city(expectedCity)
                .build();

        // THEN
        assertEquals(expectedId, id.get(address));
        assertEquals(expectedStreet, street.get(address));
        assertEquals(expectedDistrict, district.get(address));
        assertEquals(expectedCity, city.get(address));
    }


    @Test
    void testSetters() throws IllegalAccessException {
        // WHEN
        address.setId(expectedId);
        address.setStreet(expectedStreet);
        address.setDistrict(expectedDistrict);
        address.setCity(expectedCity);

        // THEN
        assertEquals(expectedId, id.get(address));
        assertEquals(expectedStreet, street.get(address));
        assertEquals(expectedDistrict, district.get(address));
        assertEquals(expectedCity, city.get(address));
    }


    @Test
    void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(address, expectedId);
        street.set(address, expectedStreet);
        district.set(address, expectedDistrict);
        city.set(address, expectedCity);

        // THEN
        assertEquals(expectedId, address.getId());
        assertEquals(expectedStreet, address.getStreet());
        assertEquals(expectedDistrict, address.getDistrict());
        assertEquals(expectedCity, address.getCity());
    }

    @Test
    void testToString() throws IllegalAccessException {
        // WHEN
        id.set(address, expectedId);
        street.set(address, expectedStreet);
        district.set(address, expectedDistrict);
        city.set(address, expectedCity);

        // THEN
        String expectedString = "Trần Phú, Hà Đông, Hà Nội";
        assertEquals(expectedString, address.toString());
    }

}