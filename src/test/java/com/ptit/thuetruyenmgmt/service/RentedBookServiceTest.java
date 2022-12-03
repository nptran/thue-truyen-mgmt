package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.exception.FailedToResetBookPenaltiesException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.impl.RentedBookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

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

        verify(repository, times(1)).findAllByCustomer_IdAndIsPaidIsFalse(1);

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
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

        verify(repository, times(1)).findAllByCustomer_IdAndIsPaidIsFalse(1);

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
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
                .penalties(mockRBP)
                .isPaid(false).build();

        when(repository.findById(1)).thenReturn(Optional.of(mockRentedBook));
        when(rentedBookPenaltyRepository.findAllByRentedBook_Id(1)).thenReturn(mockRBP);

        RentedBook received = service.getRentedBookById(1);

        assertEquals(received, mockRentedBook);

        verify(repository, times(1)).findById(1);
        verify(rentedBookPenaltyRepository, times(1)).findAllByRentedBook_Id(1);

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
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

        verify(repository, times(1)).findById(1);
        verify(rentedBookPenaltyRepository, times(1)).findAllByRentedBook_Id(1);

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * {@link RentedBookServiceImpl#getRentedBookById(int)}
     *
     * @NEGATIVE_CASE: Không tìm thấy RentedBook
     */
    @Test
    void whenGetRentedBookById_notFoundRentedBook() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRentedBookById(1))
                .isInstanceOf(NotFoundException.class);

        verify(repository, times(1)).findById(1);

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
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

        for (int id : ids) {
            verify(repository, times(1)).findById(id);
        }

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
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
            verify(repository, times(1)).findById(id);
        }

        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
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

        List<Integer> ids = getListOfInts(3, 6);
        List<RentedBook> received = service.getRentedBooksById(ids);

        assertIterableEquals(received, new ArrayList<>());

        for (int id : ids) {
            verify(repository, times(1)).findById(id);
        }
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
    }

    @Test
    void whenGetRentedBooksById_noIds() {
        // WHEN NULL ids
        List<RentedBook> received = service.getRentedBooksById(null);
        assertIterableEquals(received, new ArrayList<>());

        // WHEN empty Ids
        received = service.getRentedBooksById(new ArrayList<>());
        assertIterableEquals(received, new ArrayList<>());

        verifyNoInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
    }

    /**
     *
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_shouldResetTheRentedBookPenalties() {
        List<RentedBookPenaltyKey> ids = getListOfRentedBookPenaltyKey(0, 3);
        // Mock để penalties không empty
        List<RentedBookPenalty> penalties = new ArrayList<>();
        RentedBookPenalty mockPen = new RentedBookPenalty();
        penalties.add(mockPen);

        RentedBook mock = RentedBook.builder().id(1).build();

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
        doNothing().when(rentedBookPenaltyRepository).flush();
        when(rentedBookPenaltyRepository.saveAllAndFlush(penalties)).thenReturn(penalties);

        when(repository.findById(1)).thenReturn(Optional.of(mock));

        RentedBook received = service.addPenaltiesIntoRentedBook(penalties, ids, mock.getId());

        assertEquals(mock, received);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(rentedBookPenaltyRepository, times(1)).flush();
        verify(rentedBookPenaltyRepository, times(1)).saveAllAndFlush(penalties);
        verify(repository, times(1)).findById(1);

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * Có lỗi trong khi delete.
     * Throw FailedToResetBookPenaltiesException
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_failedDelete_shouldThrowFailedToResetBookPenaltiesException() {
        List<RentedBookPenalty> penalties = new ArrayList<>();
        RentedBook mock = RentedBook.builder().id(1).build();
        List<RentedBookPenaltyKey> ids = getListOfRentedBookPenaltyKey(0, 3);

        doThrow(RuntimeException.class).when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
//        doNothing().when(rentedBookPenaltyRepository).flush();
//        doNothing().when(rentedBookPenaltyRepository).saveAllAndFlush(penalties);
//
//        when(repository.findById(1)).thenReturn(Optional.of(mock));

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> service.addPenaltiesIntoRentedBook(penalties, ids, mock.getId()))
                .isInstanceOf(FailedToResetBookPenaltiesException.class);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(ids);

        verifyNoInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * Có lỗi trong khi flush.
     * Throw FailedToResetBookPenaltiesException
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_failedFlush_shouldThrowFailedToResetBookPenaltiesException() {
        List<RentedBookPenalty> penalties = new ArrayList<>();
        RentedBook mock = RentedBook.builder().id(1).build();
        List<RentedBookPenaltyKey> ids = getListOfRentedBookPenaltyKey(0, 3);

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
        doThrow(RuntimeException.class).when(rentedBookPenaltyRepository).flush();

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> service.addPenaltiesIntoRentedBook(penalties, ids, mock.getId()))
                .isInstanceOf(FailedToResetBookPenaltiesException.class);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(rentedBookPenaltyRepository, times(1)).flush();

        verifyNoInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * Có lỗi trong khi save.
     * Throw FailedToResetBookPenaltiesException
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_failedSave_shouldThrowFailedToResetBookPenaltiesException() {
        RentedBook mock = RentedBook.builder().id(1).build();
        List<RentedBookPenaltyKey> ids = getListOfRentedBookPenaltyKey(0, 3);
        // Mock để penalties không empty
        List<RentedBookPenalty> penalties = new ArrayList<>();
        RentedBookPenalty mockPen = new RentedBookPenalty();
        penalties.add(mockPen);

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
        doNothing().when(rentedBookPenaltyRepository).flush();
        when(rentedBookPenaltyRepository.saveAllAndFlush(penalties)).thenThrow(new RuntimeException());

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> service.addPenaltiesIntoRentedBook(penalties, ids, mock.getId()))
                .isInstanceOf(FailedToResetBookPenaltiesException.class);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(rentedBookPenaltyRepository, times(1)).flush();
        verify(rentedBookPenaltyRepository, times(1)).saveAllAndFlush(penalties);

        verifyNoInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * Không tìm thấy RentedBook.
     * Throw NotFoundException
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_failedFindRentedBook_shouldThrowNotFoundException() {
        List<RentedBookPenalty> penalties = new ArrayList<>();
        RentedBookPenalty mockPen = new RentedBookPenalty();
        penalties.add(mockPen);
        RentedBook mock = RentedBook.builder().id(1).build();
        List<RentedBookPenaltyKey> ids = getListOfRentedBookPenaltyKey(0, 3);

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(ids);
        doNothing().when(rentedBookPenaltyRepository).flush();
        when(rentedBookPenaltyRepository.saveAllAndFlush(penalties)).thenReturn(penalties);
        when(repository.findById(1)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> service.addPenaltiesIntoRentedBook(penalties, ids, mock.getId()))
                .isInstanceOf(NotFoundException.class);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(rentedBookPenaltyRepository, times(1)).flush();
        verify(rentedBookPenaltyRepository, times(1)).saveAllAndFlush(penalties);
        verify(repository, times(1)).findById(1);

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * @NEGATIVE: Tham số DS Lỗi truyện hiện tại là rỗng
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_emptyRemovedIds() {
        RentedBook mock = RentedBook.builder().id(1).build();
        // DS Lỗi truyện hiện tại là rỗng
        List<RentedBookPenaltyKey> currIds = new ArrayList<>();

        // 2 pen mới
        List<RentedBookPenalty> newPenalties = getListOfRentedBookPenalties(mock.getId(), 1, 2, 1000);
        mock.setPenalties(newPenalties);

        when(rentedBookPenaltyRepository.saveAllAndFlush(newPenalties)).thenReturn(newPenalties);
        when(repository.findById(mock.getId())).thenReturn(Optional.of(mock));

        RentedBook received = service.addPenaltiesIntoRentedBook(newPenalties, currIds, mock.getId());

        assertEquals(mock, received);

        verify(rentedBookPenaltyRepository, times(1)).saveAllAndFlush(newPenalties);
        verify(repository, times(1)).findById(mock.getId());

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * @NEGATIVE: Tham số DS Lỗi truyện hiện tại là NULL
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_nullRemovedIds() {
        RentedBook mock = RentedBook.builder().id(1).build();
        // DS Lỗi truyện hiện tại là rỗng
        List<RentedBookPenaltyKey> currIds = null;

        // 2 pen mới
        List<RentedBookPenalty> newPenalties = getListOfRentedBookPenalties(mock.getId(), 1, 2, 1000);
        mock.setPenalties(newPenalties);

        when(rentedBookPenaltyRepository.saveAllAndFlush(newPenalties)).thenReturn(newPenalties);
        when(repository.findById(mock.getId())).thenReturn(Optional.of(mock));

        RentedBook received = service.addPenaltiesIntoRentedBook(newPenalties, currIds, mock.getId());

        assertEquals(mock, received);

        verify(rentedBookPenaltyRepository, times(1)).saveAllAndFlush(newPenalties);
        verify(repository, times(1)).findById(mock.getId());

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * @NEGATIVE: DS Lỗi truyện mới của truyện là rỗng (Clean toàn bộ lỗi của truyện).
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_emptyNewPenalties() {
        RentedBook mock = RentedBook.builder().id(1).build();
        // Lỗi hiện tại có 2
        List<RentedBookPenaltyKey> currIds = getListOfRentedBookPenaltyKey(1, 2);
        // Lỗi truyện mới là rỗng
        List<RentedBookPenalty> newPenalties = new ArrayList<>();

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(currIds);
        doNothing().when(rentedBookPenaltyRepository).flush();
        when(rentedBookPenaltyRepository.saveAllAndFlush(newPenalties)).thenReturn(newPenalties);
        when(repository.findById(mock.getId())).thenReturn(Optional.of(mock));

        RentedBook received = service.addPenaltiesIntoRentedBook(newPenalties, currIds, mock.getId());

        assertNull(received.getPenalties());
        assertEquals(mock, received);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(currIds);
        verify(rentedBookPenaltyRepository, times(1)).flush();
        verify(repository, times(1)).findById(mock.getId());

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * @NEGATIVE: DS Lỗi truyện mới của truyện là NULL (Clean toàn bộ lỗi của truyện).
     */
    @Test
    void whenAddPenaltiesIntoRentedBook_nullNewPenalties() {
        RentedBook mock = RentedBook.builder().id(1).build();
        // Lỗi hiện tại có 2
        List<RentedBookPenaltyKey> currIds = getListOfRentedBookPenaltyKey(1, 2);
        // Lỗi truyện mới là rỗng
        List<RentedBookPenalty> newPenalties = null;

        doNothing().when(rentedBookPenaltyRepository).deleteAllByIdInBatch(currIds);
        doNothing().when(rentedBookPenaltyRepository).flush();
        when(rentedBookPenaltyRepository.saveAllAndFlush(newPenalties)).thenReturn(newPenalties);
        when(repository.findById(mock.getId())).thenReturn(Optional.of(mock));

        RentedBook received = service.addPenaltiesIntoRentedBook(newPenalties, currIds, mock.getId());

        assertNull(received.getPenalties());
        assertEquals(mock, received);

        verify(rentedBookPenaltyRepository, times(1)).deleteAllByIdInBatch(currIds);
        verify(rentedBookPenaltyRepository, times(1)).flush();
        verify(repository, times(1)).findById(mock.getId());

        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(rentedBookPenaltyRepository);
    }


    /**
     * @POSITIVE: Có DS rentedBookPenalty đầu vào hợp lệ
     * → Trả về DS penalty tương ứng
     */
    @Test
    void whenRentedBookPenaltiesToPenalties_shouldReturnPenalties() {
        List<RentedBookPenalty> mock = getListOfRentedBookPenalties(1, 1, 2, 1000);

        List<Penalty> expected = new ArrayList<>();
        expected.add(Penalty.builder().id(1).recommendedFee(1000).build());
        expected.add(Penalty.builder().id(2).recommendedFee(1000).build());

        List<Penalty> actual = service.rentedBookPenaltiesToPenalties(mock);

        assertEquals(actual, expected);

        verifyNoInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
    }

    /**
     * @NEGATIVE: Có DS rentedBookPenalty đầu vào null
     * → Trả về DS penalty rỗng
     */
    @Test
    void whenRentedBookPenaltiesToPenalties_shouldReturnEmpty() {
        List<Penalty> actual = service.rentedBookPenaltiesToPenalties(null);

        assertEquals(actual, new ArrayList<>());

        verifyNoInteractions(repository);
        verifyNoInteractions(rentedBookPenaltyRepository);
    }


    private List<Integer> getListOfInts(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }


    private List<RentedBookPenaltyKey> getListOfRentedBookPenaltyKey(int fromID, int toID) {
        return IntStream.range(fromID, toID + 1)
                .mapToObj(i -> RentedBookPenaltyKey.builder().rentedBookId(i).penaltyId(i).build())
                .collect(Collectors.toList());
    }


    private List<RentedBookPenalty> getListOfRentedBookPenalties(int bookID, int fromPenID, int toPenID, double fee) {
        return IntStream.range(fromPenID, toPenID + 1)
                .mapToObj(i -> RentedBookPenalty.builder()
                        .id(new RentedBookPenaltyKey(bookID, i))
                        .fee(fee)
                        .penalty(Penalty.builder()
                                .id(i)
                                .recommendedFee(fee)
                                .build())
                        .build()
                ).collect(Collectors.toList());
    }


}
