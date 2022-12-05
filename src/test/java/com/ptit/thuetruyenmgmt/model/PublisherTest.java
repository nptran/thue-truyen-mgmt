package com.ptit.thuetruyenmgmt.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PublisherTest implements EntityTest {

    private Publisher publisher;

    private final Integer expectedId = 1;
    private final String expectedCode = "NXB001";
    private final String expectedName = "NXB. Nhi Đồng";
    private final String expectedEmail = "publisher@unittest.com";
    private final String expectedPhone = "098765434567";
    private final String expectedDescription = "Mô tả...";
    private final Address expectedAddress = Address.builder().id(1).build();
    private List<BookTitle> expectedBooks;

    private Field id;
    private Field code;
    private Field name;
    private Field email;
    private Field phone;
    private Field description;
    private Field address;
    private Field books;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException {
        publisher = new Publisher();

        BookTitle b1 = BookTitle.builder().id(1).build();
        BookTitle b2 = BookTitle.builder().id(2).build();
        BookTitle b3 = BookTitle.builder().id(3).build();
        BookTitle b4 = BookTitle.builder().id(4).build();
        BookTitle b5 = BookTitle.builder().id(5).build();
        expectedBooks = new ArrayList<>();
        expectedBooks.add(b1);
        expectedBooks.add(b2);
        expectedBooks.add(b3);
        expectedBooks.add(b4);
        expectedBooks.add(b5);

        id = publisher.getClass().getDeclaredField("id");
        code = publisher.getClass().getDeclaredField("code");
        name = publisher.getClass().getDeclaredField("name");
        email = publisher.getClass().getDeclaredField("email");
        phone = publisher.getClass().getDeclaredField("phone");
        description = publisher.getClass().getDeclaredField("description");
        address = publisher.getClass().getDeclaredField("address");
        books = publisher.getClass().getDeclaredField("books");
        id.setAccessible(true);
        code.setAccessible(true);
        name.setAccessible(true);
        email.setAccessible(true);
        phone.setAccessible(true);
        description.setAccessible(true);
        address.setAccessible(true);
        books.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        publisher = new Publisher(
                expectedId,
                expectedCode,
                expectedName,
                expectedEmail,
                expectedPhone,
                expectedDescription,
                expectedAddress,
                expectedBooks
        );

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        // WHEN
        publisher = Publisher.builder()
                .id(expectedId)
                .code(expectedCode)
                .name(expectedName)
                .email(expectedEmail)
                .phone(expectedPhone)
                .description(expectedDescription)
                .address(expectedAddress)
                .books(expectedBooks)
                .build();

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        // WHEN
        publisher.setId(expectedId);
        publisher.setCode(expectedCode);
        publisher.setName(expectedName);
        publisher.setEmail(expectedEmail);
        publisher.setPhone(expectedPhone);
        publisher.setDescription(expectedDescription);
        publisher.setAddress(expectedAddress);
        publisher.setBooks(expectedBooks);

        // THEN
        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(publisher, expectedId);
        code.set(publisher, expectedCode);
        name.set(publisher, expectedName);
        email.set(publisher, expectedEmail);
        phone.set(publisher, expectedPhone);
        description.set(publisher, expectedDescription);
        address.set(publisher, expectedAddress);
        books.set(publisher, expectedBooks);

        // THEN
        assertFields(true);
    }

    @Override
    public void testToString() throws IllegalAccessException {

    }

    @Override
    public void testEquals() throws NoSuchFieldException, IllegalAccessException {

    }

    @Override
    public void testHashCode() {

    }

    private void assertFields(boolean useGetter) throws IllegalAccessException {
        // THEN
        if (!useGetter) {
            assertEquals(expectedId, id.get(publisher));
            assertEquals(expectedCode, code.get(publisher));
            assertEquals(expectedName, name.get(publisher));
            assertEquals(expectedEmail, email.get(publisher));
            assertEquals(expectedPhone, phone.get(publisher));
            assertEquals(expectedDescription, description.get(publisher));
            assertEquals(expectedAddress, address.get(publisher));
            assertEquals(expectedBooks, books.get(publisher));
            return;
        }

        assertEquals(expectedId, publisher.getId());
        assertEquals(expectedCode, publisher.getCode());
        assertEquals(expectedName, publisher.getName());
        assertEquals(expectedEmail, publisher.getEmail());
        assertEquals(expectedPhone, publisher.getPhone());
        assertEquals(expectedDescription, publisher.getDescription());
        assertEquals(expectedAddress, publisher.getAddress());
        assertEquals(expectedBooks, publisher.getBooks());
    }

}
