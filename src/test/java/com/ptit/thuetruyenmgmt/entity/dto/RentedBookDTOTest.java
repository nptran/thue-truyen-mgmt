package com.ptit.thuetruyenmgmt.entity.dto;

import com.ptit.thuetruyenmgmt.entity.EntityTest;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.dto.RentedBookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RentedBookDTOTest implements EntityTest {

    private RentedBookDTO rentedBookDTO;

    private final int expectedCustomerId = 200;
    private final int expectedRentedBookId = 1;
    private final String expectedCode = "BT001";
    private final String expectedTitleName = "Tiêu Đề";
    private final LocalDateTime expectedRentedTime = LocalDateTime.of(2022, 10, 10, 10, 10, 10);
    private final double expectedAmount = 2000.99;
    private final double expectedAmountTilToday = 2000.99;
    private List<Penalty> expectedPenalties;


    private Field customerId;
    private Field rentedBookId;
    private Field code;
    private Field titleName;
    private Field rentedTime;
    private Field amount;
    private Field amountTilToday;
    private Field penalties;


    @BeforeEach
    @Override
    public void initData() throws NoSuchFieldException, IllegalAccessException {
        rentedBookDTO = new RentedBookDTO();

        expectedPenalties = new ArrayList<>();
        Penalty p1 = new Penalty();
        Field pId = p1.getClass().getDeclaredField("id");
        pId.setAccessible(true);
        pId.set(p1, 1);
        Penalty p2 = new Penalty();
        pId = p2.getClass().getDeclaredField("id");
        pId.setAccessible(true);
        pId.set(p2, 2);
        Penalty p3 = new Penalty();
        pId = p3.getClass().getDeclaredField("id");
        pId.setAccessible(true);
        pId.set(p3, 3);
        expectedPenalties.add(p1);
        expectedPenalties.add(p2);
        expectedPenalties.add(p3);

        customerId = rentedBookDTO.getClass().getDeclaredField("customerId");
        rentedBookId = rentedBookDTO.getClass().getDeclaredField("rentedBookId");
        code = rentedBookDTO.getClass().getDeclaredField("code");
        titleName = rentedBookDTO.getClass().getDeclaredField("titleName");
        rentedTime = rentedBookDTO.getClass().getDeclaredField("rentedTime");
        amount = rentedBookDTO.getClass().getDeclaredField("amount");
        amountTilToday = rentedBookDTO.getClass().getDeclaredField("amountTilToday");
        penalties = rentedBookDTO.getClass().getDeclaredField("penalties");

        customerId.setAccessible(true);
        rentedBookId.setAccessible(true);
        code.setAccessible(true);
        titleName.setAccessible(true);
        rentedTime.setAccessible(true);
        amount.setAccessible(true);
        amountTilToday.setAccessible(true);
        penalties.setAccessible(true);
    }

    @Test
    @Override
    public void testFullArgConstructor() throws IllegalAccessException {
        rentedBookDTO = new RentedBookDTO(
                expectedCustomerId,
                expectedRentedBookId,
                expectedCode,
                expectedTitleName,
                expectedRentedTime,
                expectedAmount,
                expectedAmountTilToday,
                expectedPenalties
        );

        assertFields(false);
    }

    @Test
    @Override
    public void testBuilder() throws IllegalAccessException {
        rentedBookDTO = RentedBookDTO.builder()
                .customerId(expectedCustomerId)
                .rentedBookId(expectedRentedBookId)
                .code(expectedCode)
                .titleName(expectedTitleName)
                .rentedTime(expectedRentedTime)
                .amount(expectedAmount)
                .amountTilToday(expectedAmountTilToday)
                .penalties(expectedPenalties)
                .build();

        assertFields(false);
    }

    @Test
    @Override
    public void testSetters() throws IllegalAccessException {
        rentedBookDTO.setCustomerId(expectedCustomerId);
        rentedBookDTO.setRentedBookId(expectedRentedBookId);
        rentedBookDTO.setCode(expectedCode);
        rentedBookDTO.setTitleName(expectedTitleName);
        rentedBookDTO.setRentedTime(expectedRentedTime);
        rentedBookDTO.setAmount(expectedAmount);
        rentedBookDTO.setAmount(expectedAmountTilToday);
        rentedBookDTO.setPenalties(expectedPenalties);

        assertFields(false);
    }

    @Test
    @Override
    public void testGetters() throws IllegalAccessException {
        customerId.set(rentedBookDTO, expectedCustomerId);
        rentedBookId.set(rentedBookDTO, expectedRentedBookId);
        code.set(rentedBookDTO, expectedCode);
        titleName.set(rentedBookDTO, expectedTitleName);
        rentedTime.set(rentedBookDTO, expectedRentedTime);
        amount.set(rentedBookDTO, expectedAmount);
        amount.set(rentedBookDTO, expectedAmountTilToday);
        penalties.set(rentedBookDTO, expectedPenalties);

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
            assertEquals(expectedCustomerId, customerId.get(rentedBookDTO));
            assertEquals(expectedRentedBookId, rentedBookId.get(rentedBookDTO));
            assertEquals(expectedCode, code.get(rentedBookDTO));
            assertEquals(expectedTitleName, titleName.get(rentedBookDTO));
            assertEquals(expectedRentedTime, rentedTime.get(rentedBookDTO));
            assertEquals(expectedAmount, amount.get(rentedBookDTO));
            assertEquals(expectedAmountTilToday, amount.get(rentedBookDTO));
            assertEquals(expectedPenalties, penalties.get(rentedBookDTO));
            return;
        }

        assertEquals(expectedCustomerId, rentedBookDTO.getCustomerId());
        assertEquals(expectedRentedBookId, rentedBookDTO.getRentedBookId());
        assertEquals(expectedCode, rentedBookDTO.getCode());
        assertEquals(expectedTitleName, rentedBookDTO.getTitleName());
        assertEquals(expectedRentedTime, rentedBookDTO.getRentedTime());
        assertEquals(expectedAmount, rentedBookDTO.getAmount());
        assertEquals(expectedAmountTilToday, rentedBookDTO.getAmount());
        assertEquals(expectedPenalties, rentedBookDTO.getPenalties());
    }

}
