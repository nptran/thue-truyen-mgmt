package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.model.request.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.model.request.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.request.ReturnRentedBookRequest;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(RentedBookController.class)
public class RentedBookControllerTest {

    @MockBean
    private RentedBookService service;

    @MockBean
    private PenaltyService penaltyService;

    @Autowired
    private MockMvc mvc;

    /**
     * {@link RentedBookController#getGDRentedBooks(Integer, HttpSession)}: Test Case 1
     *
     * @POSITIVE: KH được chọn có đầu truyện đang mượn (3).
     * → Trả về thông tin các đầu truyện đang mượn
     */
    @Test
    void whenGetGDRentedBooks_thenReturn200AndDSTruyen() throws Exception {
        String kw = "Minh"; // Tìm từ KH tên Minh
        // KH 1 có 3 đầu truyện đang mượn
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        RentedBook b1 = RentedBook.builder().id(1).customer(c1).build();
        RentedBook b2 = RentedBook.builder().id(2).customer(c1).build();
        RentedBook b3 = RentedBook.builder().id(3).customer(c1).build();
        List<RentedBook> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        books.add(b3);

        // Tạo DS chọn truyện
        ReadyToReturnBooks selected = new ReadyToReturnBooks();
        selected.setCustomerId(c1.getId());

        when(service.getRentedBooksByCustomer(c1.getId())).thenReturn(books);
        this.mvc.perform(
                        get("/customer/rented-books={id}", c1.getId())
                                .sessionAttr("kwName", kw)) // Mock giá trị cho session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-truyen-kh"))
                .andExpect(model().attribute("searchKw", kw))
                .andExpect(model().attribute("allRentedBooks", hasSize(3))) // Có 3 đầu truyện mượn
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b1.getId())), // Đúng đầu truyện cần lấy
                                hasProperty("customer", Matchers.is(c1))    // Đầu truyện của đúng KH
                        )
                )))
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b2.getId())), // Đúng đầu truyện cần lấy
                                hasProperty("customer", Matchers.is(c1))    // Đầu truyện của đúng KH
                        )
                )))
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b3.getId())), // Đúng đầu truyện cần lấy
                                hasProperty("customer", Matchers.is(c1))    // Đầu truyện của đúng KH
                        )
                )))
                .andExpect(model().attribute("selectedBooks", selected));

        verify(service, times(1)).getRentedBooksByCustomer(c1.getId());
        verifyNoMoreInteractions(service);
    }


    /**
     * {@link RentedBookController#getGDRentedBooks(Integer, HttpSession)}: Test Case 2
     *
     * @NEGATIVE: KH được chọn không có đầu truyện nào đang mượn (3).
     * → Không có thông tin các đầu truyện đang mượn
     */
    @Test
    void whenGetGDRentedBooks_thenReturn204AndDSTruyenRong() throws Exception {
        String kw = "Minh"; // Tìm từ KH tên Minh
        // KH 1 có 3 đầu truyện đang mượn
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        List<RentedBook> books = new ArrayList<>();

        when(service.getRentedBooksByCustomer(c1.getId())).thenReturn(books);
        this.mvc.perform(
                        get("/customer/rented-books={id}", c1.getId())
                                .sessionAttr("kwName", kw)) // Mock giá trị cho session
                .andExpect(status().isNoContent()) // 204
                .andExpect(view().name("gd-truyen-kh"))
                .andExpect(model().attribute("searchKw", kw))
                .andExpect(model().attribute("allRentedBooks", hasSize(0))); // DS đầu truyện mượn rỗng

        verify(service, times(1)).getRentedBooksByCustomer(c1.getId());
        verifyNoMoreInteractions(service);
    }


    /**
     * {@link RentedBookController#checkSelectedReturnBooks(Integer, ReadyToReturnBooks, HttpSession, RedirectAttributes)}: Test Case 1
     *
     * @POSITIVE: Có Truyện được chọn để trả (2)
     * → Chuyển tiếp tới trang gd-tra-truyen
     */
    @Test
    void whenCheckSelectedReturnBooksAndGetGDTraTruyen_thenRedirectToTraTruyen() throws Exception {
        // 2 truyện được chọn để trả
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        RentedBook b1 = RentedBook.builder().id(1).customer(c1).build();
        RentedBook b2 = RentedBook.builder().id(2).customer(c1).build();
        List<RentedBook> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        ReadyToReturnBooks selectedBooks = ReadyToReturnBooks.builder().customerId(c1.getId()).willBeReturnedBooks(books).build();

        this.mvc.perform(
                        post("/rented-book/selectToReturn-of={id}", c1.getId())
                                .flashAttr("selectedBooks", selectedBooks)) // Mock ds chọn
                // Mong đợi chuyển hướng
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + c1.getId()));

    }


    /**
     * {@link RentedBookController#checkSelectedReturnBooks(Integer, ReadyToReturnBooks, HttpSession, RedirectAttributes)}: Test Case 2
     *
     * @NEGATIVE: Không có Truyện nào được chọn để trả
     * → Chuyển về trang gd-truyen-kh
     */
    @Test
    void whenCheckSelectedReturnBooksAndGetGDTraTruyen_thenRedirectToDSTruyenKH() throws Exception {
        // 0 truyện được chọn để trả
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        List<RentedBook> books = new ArrayList<>();
        ReadyToReturnBooks selectedBooks = ReadyToReturnBooks.builder().customerId(c1.getId()).willBeReturnedBooks(books).build();

        this.mvc.perform(
                        post("/rented-book/selectToReturn-of={id}", c1.getId())
                                .flashAttr("selectedBooks", selectedBooks)) // Mock ds chọn
                // Mong đợi chuyển hướng
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + c1.getId()));

    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 1
     *
     * @POSITIVE: Có truyện được chọn, có truyện tồn tại, có truyện có lỗi
     * → Hiển thị được gd, ds truyện được chọn và tồn tại, các lỗi đi kèm
     */
    @Test
    void whenGetGDTraTruyen_case1() throws Exception {
        // 2 truyện được chọn để trả, có lỗi
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        RentedBookPenalty p1 = RentedBookPenalty.builder()
                .id(new RentedBookPenaltyKey(1, 1))
                .penalty(Penalty.builder().id(1).build())
                .fee(1000).build();
        RentedBookPenalty p2 = RentedBookPenalty.builder()
                .id(new RentedBookPenaltyKey(1, 2))
                .penalty(Penalty.builder().id(2).build())
                .fee(2300).build();
        List<RentedBookPenalty> penaltiesOfBook1 = new ArrayList<>();
        penaltiesOfBook1.add(p1);
        penaltiesOfBook1.add(p2);
        RentedBook b1 = RentedBook.builder()
                .id(1)
                .bookTitle(BookTitle.builder().titleName("").code("").build())
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .penalties(penaltiesOfBook1)
                .amount(1000).customer(c1).build();
        RentedBook b2 = RentedBook.builder()
                .id(2)
                .bookTitle(BookTitle.builder().titleName("").code("").build())
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .amount(2000).customer(c1).build();
        List<RentedBook> selectedBooks = new ArrayList<>();
        selectedBooks.add(b1);
        selectedBooks.add(b2);
        List<Integer> ids = new ArrayList<>();
        ids.add(b1.getId());
        ids.add(b2.getId());
        List<RentedBookDTO> selectedBookDtos = rentedBooksToRentedBookDTOs(selectedBooks);

        // Mock 4 penalties
        List<Penalty> allPenalties = getListOfPenalties(1, 4, 1000, false);
        when(service.getRentedBooksById(ids)).thenReturn(selectedBooks);
        when(service.getRentedBookById(1)).thenReturn(b1);
        List<Penalty> pens1 = rentedBookPenaltiesToPenalties(b1.getPenalties());
        when(service.rentedBookPenaltiesToPenalties(b1.getPenalties())).thenReturn(pens1);
        when(service.getRentedBookById(2)).thenReturn(b2);
        List<Penalty> pens2 = rentedBookPenaltiesToPenalties(b1.getPenalties());
        when(penaltyService.getAllPenalties()).thenReturn(allPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", c1.getId())
                                .sessionAttr("selectedBookIds", ids)) // Mock ds chọn vào session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-tra-truyen"))   // Đúng giao diện
                .andExpect(model().attribute("returnBookReq", instanceOf(ReturnRentedBookRequest.class)))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("customerId", is(c1.getId()))   // Đúng KH
                ))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("rentedBookDtos", is(selectedBookDtos)) // Đúng DS Truyện
                ))
                .andExpect(model().attribute("allPenalties", is(allPenalties)));

        verify(service, times(1)).getRentedBooksById(ids);
        verify(service, times(1)).getRentedBookById(1);
        verify(service, times(1)).rentedBookPenaltiesToPenalties(b1.getPenalties());
        verify(service, times(1)).getRentedBookById(2);
        verify(service, times(0)).rentedBookPenaltiesToPenalties(b2.getPenalties());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 2
     *
     * @POSITIVE: Có truyện được chọn, có truyện tồn tại, không truyện nào có lỗi
     * → Hiển thị được gd, ds truyện được chọn và tồn tại
     */
    @Test
    void whenGetGDTraTruyen_case2() throws Exception {
        // 2 truyện được chọn để trả không có lỗi
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        RentedBook b1 = RentedBook.builder()
                .id(1)
                .bookTitle(BookTitle.builder().titleName("").code("").build())
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .amount(1000).customer(c1).build();
        RentedBook b2 = RentedBook.builder()
                .id(2)
                .bookTitle(BookTitle.builder().titleName("").code("").build())
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .amount(2000).customer(c1).build();
        List<RentedBook> selectedBooks = new ArrayList<>();
        selectedBooks.add(b1);
        selectedBooks.add(b2);
        List<Integer> ids = new ArrayList<>();
        ids.add(b1.getId());
        ids.add(b2.getId());
        List<RentedBookDTO> selectedBookDtos = rentedBooksToRentedBookDTOs(selectedBooks);

        // Mock 4 penalties
        List<Penalty> allPenalties = getListOfPenalties(1, 4, 1000, false);
        when(service.getRentedBooksById(ids)).thenReturn(selectedBooks);
        when(service.getRentedBookById(1)).thenReturn(b1);
        when(service.getRentedBookById(2)).thenReturn(b2);
        when(penaltyService.getAllPenalties()).thenReturn(allPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", c1.getId())
                                .sessionAttr("selectedBookIds", ids)) // Mock ds chọn vào session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-tra-truyen"))   // Đúng giao diện
                .andExpect(model().attribute("returnBookReq", instanceOf(ReturnRentedBookRequest.class)))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("customerId", is(c1.getId()))   // Đúng KH
                ))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("rentedBookDtos", is(selectedBookDtos)) // Đúng DS Truyện
                ))
                .andExpect(model().attribute("allPenalties", is(allPenalties)));

        verify(service, times(1)).getRentedBooksById(ids);
        verify(service, times(1)).getRentedBookById(1);
        verify(service, times(1)).getRentedBookById(2);
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 3
     *
     * @NEGATIVE: Có truyện được chọn, nhưng tất cả đều không tồn tại/đang mượn
     * → Hiển thị được gd, ds truyện được chọn rỗng
     */
    @Test
    void whenGetGDTraTruyen_case3() throws Exception {
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        // Có 2 truyện được chọn
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);

        // Mock 4 penalties
        List<Penalty> allPenalties = getListOfPenalties(1, 4, 1000, false);
        when(service.getRentedBooksById(ids)).thenReturn(new ArrayList<>()); // Nhưng DS truyện được trả về từ Service là rỗng
        when(penaltyService.getAllPenalties()).thenReturn(allPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", c1.getId())
                                .sessionAttr("selectedBookIds", ids)) // Mock ds chọn vào session
                .andExpect(status().isNoContent())  // 204
                .andExpect(view().name("gd-tra-truyen"))   // Kiểm tra giao diện
                .andExpect(model().attribute("returnBookReq", instanceOf(ReturnRentedBookRequest.class)))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("customerId", is(c1.getId()))   // Kiểm tra KH
                ))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("rentedBookDtos", emptyCollectionOf(RentedBookDTO.class)) // Kiểm tra DS Truyện
                ))
                .andExpect(model().attribute("allPenalties", is(allPenalties)));

        verify(service, times(1)).getRentedBooksById(ids);
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 3
     *
     * @NEGATIVE: Không có truyện được chọn (Khi NV truy cập trực tiếp bằng URL)
     * → Hiển thị được gd, ds truyện được chọn rỗng
     */
    @Test
    void whenGetGDTraTruyen_case4() throws Exception {
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        // Mock 4 penalties
        List<Penalty> allPenalties = getListOfPenalties(1, 4, 1000, false);
        when(service.getRentedBooksById(null)).thenReturn(new ArrayList<>()); // Không có ids
        when(penaltyService.getAllPenalties()).thenReturn(allPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", c1.getId()))
                .andExpect(status().isNoContent())  // 204
                .andExpect(view().name("gd-tra-truyen"))   // Kiểm tra giao diện
                .andExpect(model().attribute("returnBookReq", instanceOf(ReturnRentedBookRequest.class)))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("customerId", is(c1.getId()))   // Kiểm tra KH
                ))
                .andExpect(model().attribute("returnBookReq",
                        hasProperty("rentedBookDtos", emptyCollectionOf(RentedBookDTO.class)) // Kiểm tra DS Truyện
                ))
                .andExpect(model().attribute("allPenalties", is(allPenalties)));

        verify(service, times(1)).getRentedBooksById(null);
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 1
     *
     * @POSITIVE: Đầu truyện tồn tại, chưa có lỗi nào. >=1 Lỗi truyện có sẵn
     * → Hiển thị gd-cap-nhat-loi-truyen, lỗi truyện hiện tại rỗng (all ids = null)
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case1() throws Exception {
        // Truyện hiện tại chưa có lỗi
        RentedBook book = RentedBook.builder()
                .id(1)
                .customer(Customer.builder().id(1).build())
                .bookTitle(BookTitle.builder().code("").titleName("").build())
                .build();

        List<Penalty> allAvailablePenalties = getListOfPenalties(1, 5, 1000, false);
        List<Penalty> tmpPenalties = getListOfPenalties(1, 5, 1000, false);
        // Do truyện chưa có lỗi nên id = null
        for (Penalty p : tmpPenalties) {
            p.setId(null);
        }

        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(service.rentedBookPenaltiesToPenalties(book.getPenalties()))
                .thenReturn(new ArrayList<>()); // Chưa có lỗi nên trả về empty
        when(penaltyService.getAllPenalties()).thenReturn(allAvailablePenalties);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // Đầu truyện mã 1
                .andExpect(status().isOk())
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong đợi trả về gd này
                .andExpect(model().attribute("rentedBook", is(expectedDto))) // Kiểm tra object trong form
                .andExpect(model().attribute("allPenalties", is(allAvailablePenalties)));

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(service, times(1)).rentedBookPenaltiesToPenalties(book.getPenalties());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 2
     *
     * @POSITIVE: Đầu truyện tồn tại, đã có lỗi truyện. >=1 Lỗi truyện có sẵn
     * → Hiển thị gd-cap-nhat-loi-truyen, lỗi truyện hiện tại được bao gồm (id != null)
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case2() throws Exception {
        // Truyện hiện tại đã có lỗi
        RentedBook book = RentedBook.builder()
                .id(1)
                .customer(Customer.builder().id(1).build())
                .penalties(getListOfRentedBookPenalties(1, 1, 2, 500)) // Lỗi truyện của truyện
                .bookTitle(BookTitle.builder().code("").titleName("").build())
                .build();

        List<Penalty> tmpPenalties = getListOfPenalties(1, 2, 500, false); // Lỗi truyện của truyện
        List<Penalty> allAvailablePenalties = new ArrayList<>(tmpPenalties); // tất cả lỗi có sẵn

        tmpPenalties.addAll(getListOfPenalties(3,5,1000,true));
        allAvailablePenalties.addAll(getListOfPenalties(3, 5, 1000, false));

        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(service.rentedBookPenaltiesToPenalties(book.getPenalties()))
                .thenReturn(tmpPenalties); // Trả về 2 lỗi hiện có
        when(penaltyService.getAllPenalties()).thenReturn(allAvailablePenalties);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // Đầu truyện mã 1
                .andExpect(status().isOk())
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong đợi trả về gd này
                .andExpect(model().attribute("rentedBook", is(expectedDto))) // Kiểm tra object trong form
                .andExpect(model().attribute("allPenalties", is(allAvailablePenalties)));

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(service, times(1)).rentedBookPenaltiesToPenalties(book.getPenalties());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 3
     *
     * @NEGATIVE: Đầu truyện tồn tại, nhưng không có Lỗi truyện có sẵn nảo
     * → Hiển thị gd-cap-nhat-loi-truyen, http code = 204
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case3() throws Exception {
        // Truyện hiện tại đã có lỗi
        RentedBook book = RentedBook.builder()
                .id(1)
                .customer(Customer.builder().id(1).build())
                .bookTitle(BookTitle.builder().code("").titleName("").build())
                .build();

        List<Penalty> allAvailablePenalties = new ArrayList<>(); // lỗi có sẵn trống
        List<Penalty> tmpPenalties = new ArrayList<>(); // lỗi của truyện trống

        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(service.rentedBookPenaltiesToPenalties(book.getPenalties())).thenReturn(tmpPenalties);
        when(penaltyService.getAllPenalties()).thenReturn(allAvailablePenalties);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // Đầu truyện mã 1
                .andExpect(status().isNoContent()) // 204
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong đợi trả về gd này
                .andExpect(model().attribute("rentedBook", is(expectedDto))) // Kiểm tra object trong form
                .andExpect(model().attribute("allPenalties", is(allAvailablePenalties)));   // Kiểm tra lỗi có sẵn

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(service, times(1)).rentedBookPenaltiesToPenalties(book.getPenalties());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)} (Integer)}: Test Case 3
     *
     * @NEGATIVE: Đầu truyện không tồn tại, http code = 404
     * → Hiển thị gd-cap-nhat-loi-truyen, http code = 404
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case4() throws Exception {
        // Truyện không tồn tại
        RentedBook book = RentedBook.builder()
                .id(1)
                .customer(Customer.builder().id(1).build())
                .bookTitle(BookTitle.builder().code("").titleName("").build())
                .build();

        when(service.getRentedBookById(book.getId())).thenThrow(NotFoundException.class);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // Đầu truyện mã 1
                .andExpect(status().isNotFound()) // 404
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong đợi trả về gd này
                .andExpect(model().attribute("rentedBook", nullValue())) // Kiểm tra object trong form
                .andExpect(model().attribute("allPenalties", nullValue()));   // Kiểm tra lỗi có sẵn

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(service, times(0)).rentedBookPenaltiesToPenalties(anyList());
        verify(penaltyService, times(0)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#saveRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, HttpSession, Integer)}: Test Case 1
     *
     * @POSITIVE: Có lỗi truyện được chọn lưu, không phí nào bị bỏ trống
     * → Lưu thành công, chuyển về gd-tra-truyen
     */
    @Test
    void whenSaveRentedBook_thenReturn201AndRedirectToGDTraTruyen() throws Exception {
        // NV chọn 2 lỗi truyện với phí 1000
        List<Penalty> selectedPenalties = getListOfPenalties(1, 2, 1000, false);
        // Mock 2 Penalty gốc tương ứng với phí phạt 500
        List<Penalty> originalPenalties = getListOfPenalties(1, 2, 500, false);

        RentedBookDTO mockDto =
                RentedBookDTO.builder()
                        .customerId(1)
                        .rentedBookId(1)
                        .code("CODE")
                        .titleName("NAME")
                        .penalties(selectedPenalties)
                        .build();
        // Mock entity lấy từ service hiện chưa có lỗi nào
        RentedBook expectedBookBefore = RentedBook.builder()
                .id(mockDto.getRentedBookId())
                .customer(Customer.builder().id(mockDto.getCustomerId()).build())
                .bookTitle(BookTitle.builder().code(mockDto.getCode()).titleName(mockDto.getTitleName()).build())
                .build();

        // Mock RentedBookPenalty
        List<RentedBookPenalty> newPenalties = new ArrayList<>();
        for (Penalty originalPenalty : originalPenalties) {
            RentedBookPenalty rentedBookPenalty =
                    RentedBookPenalty.builder()
                            .id(new RentedBookPenaltyKey(expectedBookBefore.getId(), originalPenalty.getId()))
                            .rentedBook(expectedBookBefore)
                            .penalty(originalPenalty)
                            .fee(1000) // Phí phạt NV chọn là 1000
                            .build();
            newPenalties.add(rentedBookPenalty);
        }

        // Mock entity lấy từ service sau khi thêm lỗi
        RentedBook expectedBookAfter = RentedBook.builder()
                .id(mockDto.getRentedBookId())
                .customer(Customer.builder().id(mockDto.getCustomerId()).build())
                .penalties(newPenalties)
                .bookTitle(BookTitle.builder().code(mockDto.getCode()).titleName(mockDto.getTitleName()).build())
                .build();

        when(service.getRentedBookById(mockDto.getRentedBookId())).thenReturn(expectedBookBefore);
        for (Penalty p : originalPenalties) {
            when(penaltyService.getPenaltyById(p.getId()))
                    .thenReturn(p);
        }
        when(service.addPenaltiesIntoRentedBook(newPenalties, new ArrayList<>(), expectedBookBefore.getId())).thenReturn(expectedBookAfter);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", mockDto.getRentedBookId())
                                .flashAttr("rentedBook", mockDto)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Trước đó NV chọn 3 truyện để trả
                )
                // Mong đợi quay lai giao diện tra-truyen
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + expectedBookAfter.getCustomer().getId()));

        verify(service, times(1)).getRentedBookById(mockDto.getRentedBookId());
        for (Penalty p : originalPenalties) {
            verify(penaltyService, times(1)).getPenaltyById(p.getId());
        }
        verify(service, times(1)).addPenaltiesIntoRentedBook(newPenalties, new ArrayList<>(), expectedBookBefore.getId());

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#saveRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, HttpSession, Integer)}: Test Case 2
     *
     * @POSITIVE: Không có lỗi truyện nào được chọn lưu
     * → Lưu thành công, chuyển về gd-tra-truyen
     */
    @Test
    void whenSaveRentedBook_case2() throws Exception {
        // NV không chọn lỗi truyện nào (id = null)
        List<Penalty> selectedPenalties = getListOfPenalties(1, 2, 1000, true);
        // Mock 2 Penalty gốc tương ứng với phí phạt 500
        List<Penalty> originalPenalties = getListOfPenalties(1, 2, 500, false);

        RentedBookDTO mockDto =
                RentedBookDTO.builder()
                        .customerId(1)
                        .rentedBookId(1)
                        .code("CODE")
                        .titleName("NAME")
                        .penalties(selectedPenalties)
                        .build();
        // Mock entity lấy từ service hiện chưa có lỗi nào
        RentedBook expectedBookBefore = RentedBook.builder()
                .id(mockDto.getRentedBookId())
                .customer(Customer.builder().id(mockDto.getCustomerId()).build())
                .bookTitle(BookTitle.builder().code(mockDto.getCode()).titleName(mockDto.getTitleName()).build())
                .build();

        // Mock lỗi mới là rỗng
        List<RentedBookPenalty> newPenalties = new ArrayList<>();

        // Mock entity lấy từ service sau cập nhật lỗi
        RentedBook expectedBookAfter = RentedBook.builder()
                .id(mockDto.getRentedBookId())
                .customer(Customer.builder().id(mockDto.getCustomerId()).build())
                .penalties(newPenalties)
                .bookTitle(BookTitle.builder().code(mockDto.getCode()).titleName(mockDto.getTitleName()).build())
                .build();

        when(service.getRentedBookById(mockDto.getRentedBookId())).thenReturn(expectedBookBefore);
        for (Penalty p : originalPenalties) {
            when(penaltyService.getPenaltyById(p.getId()))
                    .thenReturn(p);
        }
        when(service.addPenaltiesIntoRentedBook(newPenalties, new ArrayList<>(), expectedBookBefore.getId())).thenReturn(expectedBookAfter);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", mockDto.getRentedBookId())
                                .flashAttr("rentedBook", mockDto)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Trước đó NV chọn 3 truyện để trả
                )
                // Mong đợi quay lai giao diện tra-truyen
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + expectedBookAfter.getCustomer().getId()));

        verify(service, times(1)).getRentedBookById(mockDto.getRentedBookId());
        for (Penalty p : originalPenalties) {
            verify(penaltyService, times(0)).getPenaltyById(p.getId()); // 0 lỗi truyện được lưu nên sẽ không được gọi
        }
        verify(service, times(1)).addPenaltiesIntoRentedBook(newPenalties, new ArrayList<>(), expectedBookBefore.getId());

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    // Các hàm tạo mock data:

    private List<Integer> getListOfInts(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    private List<RentedBookPenalty> getListOfRentedBookPenalties(int bookID, int fromPenID, int toPenID, double fee) {
        return IntStream.range(fromPenID, toPenID + 1)
                .mapToObj(i -> RentedBookPenalty.builder()
                        .id(new RentedBookPenaltyKey(bookID, i))
                        .fee(fee)
                        .penalty(Penalty.builder()
                                .id(i)
                                .name("Lỗi")
                                .description("")
                                .recommendedFee(fee)
                                .build())
                        .build()
                ).collect(Collectors.toList());
    }

    private List<Penalty> getListOfPenalties(int fromPenID, int toPenID, double fee, boolean idNull) {
        return IntStream.range(fromPenID, toPenID + 1)
                .mapToObj(i -> Penalty.builder()
                        .id(idNull ? null : i)
                        .name("Lỗi")
                        .description("")
                        .recommendedFee(fee)
                        .build()
                ).collect(Collectors.toList());
    }

    private List<RentedBookDTO> rentedBooksToRentedBookDTOs(List<RentedBook> rentedBooks) {
        List<RentedBookDTO> dtos = new ArrayList<>();
        for (RentedBook book : rentedBooks) {
            List<Penalty> currentPenalties = book.getPenalties() == null ? new ArrayList<>() : rentedBookPenaltiesToPenalties(book.getPenalties());

            RentedBookDTO dto =
                    RentedBookDTO.builder()
                            .rentedBookId(book.getId())
                            .code(book.getBookTitle().getCode())
                            .titleName(book.getBookTitle().getTitleName())
                            .rentedTime(book.getRentedTime())
                            .amount(book.getAmount())
                            .penalties(currentPenalties)
                            .build();
            dtos.add(dto);
        }

        return dtos;
    }

    private List<Penalty> rentedBookPenaltiesToPenalties(List<RentedBookPenalty> rentedBookPenalties) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentedBookPenalties != null) {
            for (RentedBookPenalty r : rentedBookPenalties) {
                Penalty p = r.getPenalty();
                p.setRecommendedFee(r.getFee());
                penalties.add(p);
            }
        }
        return penalties;
    }

}
