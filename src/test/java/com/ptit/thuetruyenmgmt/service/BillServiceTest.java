package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.exception.FailedToPayException;
import com.ptit.thuetruyenmgmt.exception.NoneSelectedBookToReturnException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.Staff;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.repository.BillRepository;
import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.repository.StaffRepository;
import com.ptit.thuetruyenmgmt.service.impl.BillServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
public class BillServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Mock
    private BillRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RentedBookRepository rentedBookRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private BillServiceImpl service;


    /**
     * @POSITIVE: Tìm thấy Nhân viên và DS truyện cần trả
     * → Tạo Bill info
     */
    @Test
    void createPayInfo_shouldReturnBill() {
        Staff mockStaff = Staff.staffBuilder().id(1).build();
        when(staffRepository.findById(mockStaff.getId())).thenReturn(Optional.ofNullable(mockStaff));

        // 2 truyện mượn 1 ngày trước
        // Truyện 1 có 2 lỗi
        RentedBookPenalty p1 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(1, 1)).fee(500).build();
        RentedBookPenalty p2 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(1, 2)).fee(600).build();
        List<RentedBookPenalty> pofB1 = new ArrayList<>();
        pofB1.add(p1);
        pofB1.add(p2);
        RentedBook b1 = RentedBook.builder().id(1).amount(1000).rentedTime(LocalDateTime.now().minusDays(1)).penalties(pofB1).build();
        RentedBook b2 = RentedBook.builder().id(2).amount(2000).rentedTime(LocalDateTime.now().minusDays(1)).penalties(new ArrayList<>()).build();
        List<RentedBook> mockBooks = new ArrayList<>();
        mockBooks.add(b1);
        mockBooks.add(b2);

        Bill actual = service.createPayInfo(mockBooks, mockStaff.getId());
        assertNull(actual.getId());
        assertEquals(4100, actual.getTotalAmount());
        assertEquals(mockBooks, actual.getRentedBooks());
        assertEquals(mockStaff, actual.getStaff());

        verify(staffRepository, times(1)).findById(mockStaff.getId());
    }


    /**
     * @NEGATIVE: Không tìm thấy Nhân viên
     * → {@link NotFoundException}
     */
    @Test
    void whenCreatePayInfo_notFoundStaff_shouldThrowNotFoundException() {
        int mockStaffId = 0;
        when(staffRepository.findById(mockStaffId)).thenReturn(Optional.empty());
        // 2 truyện mượn 1 ngày trước
        // Truyện 1 có 2 lỗi
        RentedBookPenalty p1 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(1, 1)).fee(500).build();
        RentedBookPenalty p2 = RentedBookPenalty.builder().id(new RentedBookPenaltyKey(1, 2)).fee(600).build();
        List<RentedBookPenalty> pofB1 = new ArrayList<>();
        pofB1.add(p1);
        pofB1.add(p2);
        RentedBook b1 = RentedBook.builder().id(1).amount(1000).rentedTime(LocalDateTime.now().minusDays(1)).penalties(pofB1).build();
        RentedBook b2 = RentedBook.builder().id(2).amount(2000).rentedTime(LocalDateTime.now().minusDays(1)).penalties(new ArrayList<>()).build();
        List<RentedBook> mockBooks = new ArrayList<>();
        mockBooks.add(b1);
        mockBooks.add(b2);

        assertThatThrownBy(() -> service.createPayInfo(mockBooks, mockStaffId))
                .isInstanceOf(NotFoundException.class);

        verify(staffRepository, times(1)).findById(mockStaffId);
    }


    /**
     * @NEGATIVE: DS truyện trả trống
     * → {@link NoneSelectedBookToReturnException}
     */
    @Test
    void whenCreatePayInfo_emptyBooks_shouldThrowNoneSelectedBookToReturnException() {
        Staff mockStaff = Staff.staffBuilder().id(1).build();
        when(staffRepository.findById(mockStaff.getId())).thenReturn(Optional.ofNullable(mockStaff));

        assertThatThrownBy(() -> service.createPayInfo(new ArrayList<>(), mockStaff.getId()))
                .isInstanceOf(NoneSelectedBookToReturnException.class);

        verify(staffRepository, times(1)).findById(1);
    }


    /**
     * @NEGATIVE: DS truyện trả NULL
     * → {@link NoneSelectedBookToReturnException}
     */
    @Test
    void whenCreatePayInfo_nullBooks_shouldThrowNoneSelectedBookToReturnException() {
        Staff mockStaff = Staff.staffBuilder().id(1).build();
        when(staffRepository.findById(mockStaff.getId())).thenReturn(Optional.ofNullable(mockStaff));

        assertThatThrownBy(() -> service.createPayInfo(null, mockStaff.getId()))
                .isInstanceOf(NoneSelectedBookToReturnException.class);

        verify(staffRepository, times(1)).findById(1);
    }


    /**
     * @POSITIVE: Lưu thanh toán thành công
     * → true
     */
    @Test
    void whenSaveBillInfo_shouldReturnTrue() {
        RentedBook mockB1 = new RentedBook();
        mockB1.setId(1);
        mockB1.setPaid(false);
        RentedBook mockB2 = new RentedBook();
        mockB2.setId(2);
        mockB2.setPaid(false);
        List<RentedBook> mockBooks = new ArrayList<>();
        mockBooks.add(mockB1);
        mockBooks.add(mockB2);

        Bill mock = Bill.builder().rentedBooks(mockBooks).build();
        when(repository.save(mock)).thenReturn(mock);
        for (RentedBook b : mockBooks) {
            when(rentedBookRepository.findById(b.getId())).thenReturn(Optional.of(b));
        }
        when(rentedBookRepository.saveAllAndFlush(mockBooks)).thenReturn(mockBooks);

        assertTrue(service.saveBillInfo(mock));

        verify(repository, times(1)).save(mock);
        for (RentedBook b : mockBooks) {
            verify(rentedBookRepository, times(1)).findById(b.getId());
        }
        verify(rentedBookRepository, times(1)).saveAllAndFlush(mockBooks);
    }


    /**
     * @NEGATIVE: Lưu thanh toán thất bại do lỗi save Bill
     * → {@link com.ptit.thuetruyenmgmt.exception.FailedToPayException}
     */
    @Test
    void whenSaveBillInfo_cannotSave_shouldThrowFailedToPayException() {
        Bill mock = new Bill();

        RentedBook mockRentedBook = RentedBook.builder().id(1).build();
        List<RentedBook> books = new ArrayList<>();
        books.add(mockRentedBook);
        mock.setRentedBooks(books);
        when(repository.save(mock)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> service.saveBillInfo(mock))
                .isInstanceOf(FailedToPayException.class);

        verify(repository, times(1)).save(mock);
        verify(rentedBookRepository, times(0)).findById(anyInt());
        verify(rentedBookRepository, times(0)).saveAllAndFlush(anyList());
    }


    /**
     * @NEGATIVE: Lưu thanh toán thất bại do không tìm thấy đầu truyện
     * → {@link FailedToPayException}
     */
    @Test
    void whenSaveBillInfo_notFoundBook_shouldThrowFailedToPayException() {
        RentedBook mockB1 = new RentedBook();
        mockB1.setId(1);
        mockB1.setPaid(false);
        RentedBook mockB2 = new RentedBook();
        mockB2.setId(2);
        mockB2.setPaid(false);
        List<RentedBook> mockBooks = new ArrayList<>();
        mockBooks.add(mockB1);
        mockBooks.add(mockB2);

        Bill mock = Bill.builder().rentedBooks(mockBooks).build();
        when(repository.save(mock)).thenReturn(mock);
        // Ngẫu nhiên mock một book không tìm thấy
        Random rand = new Random();
        int index = rand.nextInt(mockBooks.size()) + 1;
        for (RentedBook b : mockBooks) {
            if (b.getId() == index) {
                when(rentedBookRepository.findById(b.getId())).thenReturn(Optional.empty());
                break;
            }
            when(rentedBookRepository.findById(b.getId())).thenReturn(Optional.of(b));
        }
        when(rentedBookRepository.saveAllAndFlush(mockBooks)).thenReturn(mockBooks);

        assertThatThrownBy(() -> service.saveBillInfo(mock))
                .isInstanceOf(FailedToPayException.class);

        verify(repository, times(1)).save(mock);
        verify(rentedBookRepository, times(index)).findById(anyInt());
        verify(rentedBookRepository, times(0)).saveAllAndFlush(anyList());
    }


    /**
     * @NEGATIVE: Lưu thanh toán thất bại do cập nhật đầu truyện xảy ra lỗi
     * → {@link FailedToPayException}
     */
    @Test
    void whenSaveBillInfo_cannotUpdateRentedBookStatus_shouldThrowFailedToPayException() {
        RentedBook mockB1 = new RentedBook();
        mockB1.setId(1);
        mockB1.setPaid(false);
        RentedBook mockB2 = new RentedBook();
        mockB2.setId(2);
        mockB2.setPaid(false);
        List<RentedBook> mockBooks = new ArrayList<>();
        mockBooks.add(mockB1);
        mockBooks.add(mockB2);

        Bill mock = Bill.builder().rentedBooks(mockBooks).build();
        when(repository.save(mock)).thenReturn(mock);
        for (RentedBook b : mockBooks) {
            when(rentedBookRepository.findById(b.getId())).thenReturn(Optional.of(b));
        }
        when(rentedBookRepository.saveAllAndFlush(mockBooks)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> service.saveBillInfo(mock))
                .isInstanceOf(FailedToPayException.class);

        verify(repository, times(1)).save(mock);
        for (RentedBook b : mockBooks) {
            verify(rentedBookRepository, times(1)).findById(b.getId());
        }
        verify(rentedBookRepository, times(1)).saveAllAndFlush(mockBooks);
    }


    /**
     * @NEGATIVE: Lưu thanh toán thất bại do ds đầu truyện rỗng
     * → {@link com.ptit.thuetruyenmgmt.exception.NoneSelectedBookToReturnException}
     */
    @Test
    void whenSaveBillInfo_emptyBooks_shouldThrowNoneSelectedBookToReturnException() {
        // DS Rỗng
        List<RentedBook> mockBooks = new ArrayList<>();

        Bill mockBill = Bill.builder().rentedBooks(mockBooks).build();
        assertThatThrownBy(() -> service.saveBillInfo(mockBill))
                .isInstanceOf(NoneSelectedBookToReturnException.class);

        verify(repository, times(0)).save(any());
        verify(rentedBookRepository, times(0)).findById(anyInt());
        verify(rentedBookRepository, times(0)).saveAllAndFlush(anyList());
    }


    /**
     * @NEGATIVE: Lưu thanh toán thất bại do ds đầu truyện NULL
     * → {@link com.ptit.thuetruyenmgmt.exception.NoneSelectedBookToReturnException}
     */
    @Test
    void whenSaveBillInfo_nullBooks_shouldThrowFailedToPayException() {
        // DS null
        Bill mockBill = Bill.builder().rentedBooks(null).build();
        assertThatThrownBy(() -> service.saveBillInfo(mockBill))
                .isInstanceOf(NoneSelectedBookToReturnException.class);

        verify(repository, times(0)).save(any());
        verify(rentedBookRepository, times(0)).findById(anyInt());
        verify(rentedBookRepository, times(0)).saveAllAndFlush(anyList());
    }
    
}
