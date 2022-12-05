package com.ptit.thuetruyenmgmt.model.dto;

import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RentedBookDTOTest implements EntityTest {

    private RentedBookDTO rentedBookDTO;

    private final int expectedCustomerId = 200;
    private final int expectedRentedBookId = 1;
    private final String expectedCode = "BT001";
    private final String expectedTitleName = "Tiêu Đề";
    private final LocalDateTime expectedRentedTime = LocalDateTime.now().minusDays(2);
    private final double expectedAmount = 2000;
    private final double expectedAmountTilToday = 4000;
    private List<Penalty> expectedPenalties;
    private List<RentedBookPenalty> expectedRentedBookPenalties;


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
        Penalty p1 = Penalty.builder()
                .id(1)
                .recommendedFee(1000)
                .build();
        Penalty p2 = Penalty.builder()
                .id(2)
                .recommendedFee(2000)
                .build();
        Penalty p3 = Penalty.builder()
                .id(3)
                .recommendedFee(3000)
                .build();
        expectedPenalties.add(p1);
        expectedPenalties.add(p2);
        expectedPenalties.add(p3);

        expectedRentedBookPenalties = new ArrayList<>();
        expectedRentedBookPenalties.add(RentedBookPenalty.builder()
                .id(RentedBookPenaltyKey.builder().rentedBookId(expectedRentedBookId).penaltyId(p1.getId()).build())
                .penalty(p1)
                .build());
        expectedRentedBookPenalties.add(RentedBookPenalty.builder()
                .id(RentedBookPenaltyKey.builder().rentedBookId(expectedRentedBookId).penaltyId(p2.getId()).build())
                .penalty(p2)
                .build());
        expectedRentedBookPenalties.add(RentedBookPenalty.builder()
                .id(RentedBookPenaltyKey.builder().rentedBookId(expectedRentedBookId).penaltyId(p3.getId()).build())
                .penalty(p3)
                .build());

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
        rentedBookDTO.setAmountTilToday(expectedAmountTilToday);
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
        amountTilToday.set(rentedBookDTO, expectedAmountTilToday);
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
            assertEquals(expectedAmountTilToday, amountTilToday.get(rentedBookDTO));
            assertEquals(expectedPenalties, penalties.get(rentedBookDTO));
            return;
        }

        assertEquals(expectedCustomerId, rentedBookDTO.getCustomerId());
        assertEquals(expectedRentedBookId, rentedBookDTO.getRentedBookId());
        assertEquals(expectedCode, rentedBookDTO.getCode());
        assertEquals(expectedTitleName, rentedBookDTO.getTitleName());
        assertEquals(expectedRentedTime, rentedBookDTO.getRentedTime());
        assertEquals(expectedAmount, rentedBookDTO.getAmount());
        assertEquals(expectedAmountTilToday, rentedBookDTO.getAmountTilToday());
        assertEquals(expectedPenalties, rentedBookDTO.getPenalties());
    }

    @Test
    public void testRentedBooksToRentedBookDTOsAndCalculateAmountTilToday_whenHasPenalties() {
        List<RentedBook> rentedBooks = mockRentedBooks(1, 3, true, expectedRentedTime);
        List<RentedBookDTO> rentedBookDTOs = RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooks);

        for (RentedBookDTO dto : rentedBookDTOs) {
            assertEquals(expectedCustomerId, dto.getCustomerId());
            assertEquals(rentedBookDTOs.indexOf(dto)+1, dto.getRentedBookId());
            assertEquals(expectedCode, dto.getCode());
            assertEquals(expectedTitleName, dto.getTitleName());
            assertEquals(expectedRentedTime, dto.getRentedTime());
            assertEquals(expectedAmount, dto.getAmount());
            assertEquals(expectedAmountTilToday, dto.getAmountTilToday());
            assertEquals(expectedPenalties, dto.getPenalties());
        }
    }


    @Test
    public void testRentedBooksToRentedBookDTOsAndCalculateAmountTilToday_whenNoPenalties() {
        List<RentedBook> rentedBooks = mockRentedBooks(1, 3, false, expectedRentedTime);
        List<RentedBookDTO> rentedBookDTOs = RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooks);

        for (RentedBookDTO dto : rentedBookDTOs) {
            assertEquals(expectedCustomerId, dto.getCustomerId());
            assertEquals(rentedBookDTOs.indexOf(dto)+1, dto.getRentedBookId());
            assertEquals(expectedCode, dto.getCode());
            assertEquals(expectedTitleName, dto.getTitleName());
            assertEquals(expectedRentedTime, dto.getRentedTime());
            assertEquals(expectedAmount, dto.getAmount());
            assertEquals(expectedAmountTilToday, dto.getAmountTilToday());
            assertEquals(new ArrayList<>(), dto.getPenalties());
        }
    }


    @Test
    public void testRentedBooksToRentedBookDTOsAndCalculateAmountTilToday_whenRentedTimeLessThanOneDay() {
        LocalDateTime mockRentedTime = LocalDateTime.now();
        List<RentedBook> rentedBooks = mockRentedBooks(1, 3, true, mockRentedTime);
        List<RentedBookDTO> rentedBookDTOs = RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooks);

        for (RentedBookDTO dto : rentedBookDTOs) {
            assertEquals(expectedCustomerId, dto.getCustomerId());
            assertEquals(rentedBookDTOs.indexOf(dto)+1, dto.getRentedBookId());
            assertEquals(expectedCode, dto.getCode());
            assertEquals(expectedTitleName, dto.getTitleName());
            assertEquals(mockRentedTime, dto.getRentedTime());
            assertEquals(expectedAmount, dto.getAmount());
            assertEquals(expectedAmount, dto.getAmountTilToday()); // Mong đợi tiền thuê tối thiểu là 1 ngày
            assertEquals(expectedPenalties, dto.getPenalties());
        }
    }


    private List<RentedBook> mockRentedBooks(int fromID, int toID, boolean hasPenalties, LocalDateTime rentedTime) {
        BookTitle seed = BookTitle.builder()
                .code(expectedCode)
                .titleName(expectedTitleName)
                .build();
        if (hasPenalties)
            return IntStream.range(fromID, toID + 1)
                    .mapToObj(i -> RentedBook.builder()
                            .id(i)
                            .bookTitle(seed)
                            .customer(Customer.builder().id(expectedCustomerId).build())
                            .rentedTime(rentedTime)
                            .amount(expectedAmount)
                            .penalties(expectedRentedBookPenalties)
                            .build()
                    ).collect(Collectors.toList());

        return IntStream.range(fromID, toID + 1)
                .mapToObj(i -> RentedBook.builder()
                        .id(i)
                        .bookTitle(seed)
                        .customer(Customer.builder().id(expectedCustomerId).build())
                        .rentedTime(rentedTime)
                        .amount(expectedAmount)
                        .build()
                ).collect(Collectors.toList());
    }

//    private List<Penalty> mockListOfPenalties(int fromPenID, int toPenID, double fee, boolean idNull) {
//        return IntStream.range(fromPenID, toPenID + 1)
//                .mapToObj(i -> Penalty.builder()
//                        .id(idNull ? null : i)
//                        .name("Lỗi")
//                        .description("")
//                        .recommendedFee(fee)
//                        .build()
//                ).collect(Collectors.toList());
//    }
//
//    private List<RentedBookPenalty> mockListOfRentedBookPenalties(int bookID, int fromPenID, int toPenID, double fee) {
//        return IntStream.range(fromPenID, toPenID + 1)
//                .mapToObj(i -> RentedBookPenalty.builder()
//                        .id(new RentedBookPenaltyKey(bookID, i))
//                        .fee(fee)
//                        .penalty(Penalty.builder()
//                                .id(i)
//                                .name("Lỗi")
//                                .description("")
//                                .recommendedFee(fee)
//                                .build())
//                        .build()
//                ).collect(Collectors.toList());
//    }

}
