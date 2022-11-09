package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.repository.PenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.impl.CustomerServiceImpl;
import com.ptit.thuetruyenmgmt.service.impl.PenaltyServiceImpl;
import com.ptit.thuetruyenmgmt.service.impl.RentedBookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RentedBookServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private RentedBookRepository repository;

    @Mock
    private RentedBookPenaltyRepository rentedBookPenaltyRepository;

    @InjectMocks
    private RentedBookServiceImpl service;


    /**
     * {@link RentedBookServiceImpl#getRentedBooksByCustomer(int)}
     *
     * @POSITIVE_CASE: KH đang mượn vài đầu truyện
     * Repository có trả về ds RentedBook của KH
     */
    @Test
    void whenGetRentedBooksByCustomer_haveRentedBooks() {
        List<RentedBook> mock = IntStream.range(0, 5)
                .mapToObj(i -> RentedBook.builder().id(i).customer(Customer.builder().id(1).build()).build())
                .collect(Collectors.toList());
        Mockito.when(repository.findAllByCustomer_IdAndIsPaidIsFalse(1)).thenReturn(mock);
        List<RentedBook> received = service.getRentedBooksByCustomer(1);

        Assertions.assertIterableEquals(received, mock);

        Mockito.verify(repository).findAllByCustomer_IdAndIsPaidIsFalse(1);
    }


    /**
     * {@link RentedBookServiceImpl#getRentedBooksByCustomer(int)}
     *
     * @POSITIVE_CASE: KH không mượn truyện nào hoặc KH không tồn tại
     * Repository trả về ds rỗng RentedBook
     */
    @Test
    void whenGetRentedBooksByCustomer_notHaveRentedBook() {
        Mockito.when(repository.findAllByCustomer_IdAndIsPaidIsFalse(1)).thenReturn(new ArrayList<>());
        List<RentedBook> received = service.getRentedBooksByCustomer(1);

        Assertions.assertIterableEquals(received, new ArrayList<>());

        Mockito.verify(repository).findAllByCustomer_IdAndIsPaidIsFalse(1);
    }


    /**
     * {@link RentedBookServiceImpl#getRentedBookById(int)}
     *
     * @POSITIVE_CASE: Repository trả về 1 RentedBook
     */
    @Test
    void whenGetRentedBookById_foundRentedBook_and_foundRentedBookPenalties() {
        List<RentedBookPenalty> mockRBP = IntStream.range(0, 5)
                .mapToObj(i -> RentedBookPenalty.builder()
                        .id(new RentedBookPenaltyKey(1, i))
                        .fee(100)
                        .build())
                .collect(Collectors.toList());

        RentedBook mockRentedBook = RentedBook.builder()
                .id(1)
                .amount(1000)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false).build();

        when(repository.findById(1)).thenReturn(Optional.of(mockRentedBook));
        when(rentedBookPenaltyRepository.findAllByRentedBook_Id(1)).thenReturn(mockRBP);
        mockRentedBook.setPenalties(mockRBP);

        RentedBook received = service.getRentedBookById(1);

        assertEquals(received, mockRentedBook);

        verify(repository).findById(1);
    }


    /**
     * {@link RentedBookServiceImpl#getRentedBookById(int)}
     *
     * @POSITIVE_CASE: Repository trả về 1 RentedBook
     */
    @Test
    void whenGetRentedBookById_foundRentedBook_and_notFoundRentedBookPenalties() {
        RentedBook mockRentedBook = RentedBook.builder()
                .id(1)
                .amount(1000)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false).build();

        when(repository.findById(1)).thenReturn(Optional.of(mockRentedBook));
        when(rentedBookPenaltyRepository.findAllByRentedBook_Id(1)).thenReturn(new ArrayList<>());
        mockRentedBook.setPenalties(new ArrayList<>());

        RentedBook received = service.getRentedBookById(1);

        assertEquals(received, mockRentedBook);

        verify(repository).findById(1);
    }


    /**
     * {@link RentedBookServiceImpl#getRentedBookById(int)}
     *
     * @NEGATIVE_CASE: Không tìm thấy RentedBook
     */
    @Test
    void whenGetRentedBookById_notFoundRentedBook() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(null));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getRentedBookById(1))
                .isInstanceOf(NotFoundException.class);

        verify(repository).findById(1);
    }


    @Test
    void whenGetRentedBooksById_foundAllProvidedIds() {
        List<RentedBook> mock = IntStream.range(0, 5)
                .mapToObj(i -> RentedBook.builder()
                        .id(i)
                        .amount(1000)
                        .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                        .isPaid(false).build())
                .collect(Collectors.toList());
        for (RentedBook m : mock) {
            when(repository.findById(m.getId())).thenReturn(Optional.of(m));
        }

        List<Integer> ids = getListOfInts(0, 5);
        List<RentedBook> received = service.getRentedBooksById(ids);

        assertIterableEquals(received, mock);

        for (RentedBook m : mock) {
            verify(repository).findById(m.getId());
        }
    }


    @Test
    void whenGetRentedBooksById_foundSomeOfProvidedIds() {
        List<RentedBook> mock = IntStream.range(0, 3)
                .mapToObj(i -> RentedBook.builder()
                        .id(i)
                        .amount(1000)
                        .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                        .isPaid(false).build())
                .collect(Collectors.toList());
        for (RentedBook m : mock) {
            when(repository.findById(m.getId())).thenReturn(Optional.of(m));
        }

        List<Integer> ids = getListOfInts(0, 5);
        List<RentedBook> received = service.getRentedBooksById(ids);

        assertIterableEquals(received, mock);

        for (int id : ids) {
            verify(repository).findById(id);
        }
    }


    @Test
    void whenGetRentedBooksById_foundNothing() {
        List<RentedBook> mock = IntStream.range(0, 3)
                .mapToObj(i -> RentedBook.builder()
                        .id(i)
                        .amount(1000)
                        .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                        .isPaid(false).build())
                .collect(Collectors.toList());
        for (RentedBook m : mock) {
            when(repository.findById(m.getId())).thenReturn(Optional.of(m));
        }

        List<Integer> ids = IntStream.range(3, 6).boxed().collect(Collectors.toList());
        List<RentedBook> received = service.getRentedBooksById(ids);

        assertIterableEquals(received, new ArrayList<>());

        for (int id : ids) {
            verify(repository).findById(id);
        }
    }

    /**
     *
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_shouldResetTheRentedBookPenalties() {
        List<RentedBookPenaltyKey> ids = IntStream.range(0, 3)
                .mapToObj(i -> RentedBookPenaltyKey.builder().rentedBookId(i).penaltyId(i).build())
                .collect(Collectors.toList());
        List<RentedBookPenalty> penalties = new ArrayList<>();
        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
        doNothing().when(rentedBookPenaltyRepository).flush();
        doNothing().when(rentedBookPenaltyRepository).saveAllAndFlush(penalties);



    }


    private List<Integer> getListOfInts(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }


}
