package com.ptit.thuetruyenmgmt.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class BookTitleTest {

    private BookTitle bookTitle;

    private final Integer expectedId = 1;

    private final String expectedCode = "BT001";

    private final String expectedTitleName = "Tiêu Đề";

    private final int expectedQuantity = 999;

    private final String expectedAuthor = "Tác Giả";
    
    private final int expectedYear = 2022;
    
    private final double expectedPrice = 1000.99;

    private Publisher expectedPublisher;

    private Set<RentedBook> expectedRentedBooks;

    private Field id;
    private Field code;
    private Field titleName;
    private Field quantity;
    private Field author;
    private Field year;
    private Field price;
    private Field publisher;
    private Field rentedBooks;

    /**
     * GIVEN
     */
    @BeforeEach
    void initData() throws NoSuchFieldException {
        bookTitle = new BookTitle();

        expectedRentedBooks = new HashSet<>();
        RentedBook book1 = RentedBook.builder()
                .id(1)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(1000).build())
                .customer(Customer.builder().id(200).build())
                .bookTitle(bookTitle)
                .isPaid(true)
                .build();
        RentedBook book2 = RentedBook.builder()
                .id(2)
                .rentedTime(LocalDateTime.of(2022, 10, 8, 8, 0, 0))
                .bookTitle(BookTitle.builder().id(2000).build())
                .customer(Customer.builder().id(200).build())
                .bookTitle(bookTitle)
                .isPaid(false)
                .build();
        expectedRentedBooks.add(book1);
        expectedRentedBooks.add(book2);

        expectedPublisher = Publisher.builder().id(10).build();

        id = bookTitle.getClass().getDeclaredField("id");
        code = bookTitle.getClass().getDeclaredField("code");
        titleName = bookTitle.getClass().getDeclaredField("titleName");
        quantity = bookTitle.getClass().getDeclaredField("quantity");
        author = bookTitle.getClass().getDeclaredField("author");
        year = bookTitle.getClass().getDeclaredField("year");
        price = bookTitle.getClass().getDeclaredField("price");
        publisher = bookTitle.getClass().getDeclaredField("publisher");
        rentedBooks = bookTitle.getClass().getDeclaredField("rentedBooks");
        // Allow accessible on private fields
        id.setAccessible(true);
        code.setAccessible(true);
        titleName.setAccessible(true);
        quantity.setAccessible(true);
        author.setAccessible(true);
        year.setAccessible(true);
        price.setAccessible(true);
        publisher.setAccessible(true);
        rentedBooks.setAccessible(true);
    }


    @Test
    void testFullArgConstructor() throws IllegalAccessException {
        // WHEN
        bookTitle = new BookTitle(expectedId, expectedCode, expectedTitleName, expectedQuantity, expectedAuthor, expectedYear, expectedPrice, expectedPublisher, expectedRentedBooks);

        // THEN
        assertEquals(expectedId, id.get(bookTitle));
        assertEquals(expectedCode, code.get(bookTitle));
        assertEquals(expectedTitleName, titleName.get(bookTitle));
        assertEquals(expectedQuantity, quantity.get(bookTitle));
        assertEquals(expectedAuthor, author.get(bookTitle));
        assertEquals(expectedYear, year.get(bookTitle));
        assertEquals(expectedPrice, price.get(bookTitle));
        assertEquals(expectedPublisher, publisher.get(bookTitle));
        assertEquals(expectedRentedBooks, rentedBooks.get(bookTitle));
    }


    @Test
    void testBuilder() throws IllegalAccessException {
        // WHEN
        bookTitle = BookTitle.builder()
                .id(expectedId)
                .code(expectedCode)
                .titleName(expectedTitleName)
                .quantity(expectedQuantity)
                .author(expectedAuthor)
                .year(expectedYear)
                .price(expectedPrice)
                .publisher(expectedPublisher)
                .rentedBooks(expectedRentedBooks)
                .build();

        // THEN
        assertEquals(expectedId, id.get(bookTitle));
        assertEquals(expectedCode, code.get(bookTitle));
        assertEquals(expectedTitleName, titleName.get(bookTitle));
        assertEquals(expectedQuantity, quantity.get(bookTitle));
        assertEquals(expectedAuthor, author.get(bookTitle));
        assertEquals(expectedYear, year.get(bookTitle));
        assertEquals(expectedPrice, price.get(bookTitle));
        assertEquals(expectedPublisher, publisher.get(bookTitle));
        assertEquals(expectedRentedBooks, rentedBooks.get(bookTitle));
    }


    @Test
    void testSetters() throws IllegalAccessException {
        // WHEN
        bookTitle.setId(expectedId);
        bookTitle.setCode(expectedCode);
        bookTitle.setTitleName(expectedTitleName);
        bookTitle.setQuantity(expectedQuantity);
        bookTitle.setAuthor(expectedAuthor);
        bookTitle.setYear(expectedYear);
        bookTitle.setPrice(expectedPrice);
        bookTitle.setPublisher(expectedPublisher);
        bookTitle.setRentedBooks(expectedRentedBooks);

        // THEN
        assertEquals(expectedId, id.get(bookTitle));
        assertEquals(expectedCode, code.get(bookTitle));
        assertEquals(expectedTitleName, titleName.get(bookTitle));
        assertEquals(expectedQuantity, quantity.get(bookTitle));
        assertEquals(expectedAuthor, author.get(bookTitle));
        assertEquals(expectedYear, year.get(bookTitle));
        assertEquals(expectedPrice, price.get(bookTitle));
        assertEquals(expectedPublisher, publisher.get(bookTitle));
        assertEquals(expectedRentedBooks, rentedBooks.get(bookTitle));
    }


    @Test
    void testGetters() throws IllegalAccessException {
        // WHEN
        id.set(bookTitle, expectedId);
        code.set(bookTitle, expectedCode);
        titleName.set(bookTitle, expectedTitleName);
        quantity.set(bookTitle, expectedQuantity);
        author.set(bookTitle, expectedAuthor);
        year.set(bookTitle, expectedYear);
        price.set(bookTitle, expectedPrice);
        publisher.set(bookTitle, expectedPublisher);
        rentedBooks.set(bookTitle, expectedRentedBooks);

        // THEN
        assertEquals(expectedId, bookTitle.getId());
        assertEquals(expectedCode, bookTitle.getCode());
        assertEquals(expectedTitleName, bookTitle.getTitleName());
        assertEquals(expectedQuantity, bookTitle.getQuantity());
        assertEquals(expectedAuthor, bookTitle.getAuthor());
        assertEquals(expectedYear, bookTitle.getYear());
        assertEquals(expectedPrice, bookTitle.getPrice());
        assertEquals(expectedPublisher, bookTitle.getPublisher());
        assertEquals(expectedRentedBooks, bookTitle.getRentedBooks());
    }

}
