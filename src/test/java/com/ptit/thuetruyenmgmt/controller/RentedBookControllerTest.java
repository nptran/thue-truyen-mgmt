package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.exception.FailedToResetBookPenaltiesException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.model.dto.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.model.dto.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.dto.ReturnRentedBookRequest;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mvc;

    @Mock
    private BindingResult bindingResult;

    private String kw;

    private Customer customer;

    private List<RentedBook> rentedBooksNoPenalties;
    private List<RentedBookDTO> rentedBooksNoPenaltiesDTOs;
    private List<Integer> rentedBookNoPenaltiesIds;

    private List<RentedBook> rentedBooksHasPenalties;
    private List<RentedBookDTO> rentedBooksHasPenaltiesDTOs;
    private List<Integer> rentedBookHasPenaltiesIds;

    private final List<RentedBook> emptyRentedBooks = new ArrayList<>();

    private ReadyToReturnBooks selectedBookReqHasPenalties;

    private ReturnRentedBookRequest selectedBookDTOReqHasPenalties;

    private ReadyToReturnBooks selectedBookReqNoPenalties;

    private ReturnRentedBookRequest selectedBookDTOReqNoPenalties;

    private ReadyToReturnBooks noneSelectedBookReq;

    private ReturnRentedBookRequest noneSelectedBookDTOReq;

    private ReadyToReturnBooks emptySelectedBookReq;

    private ReturnRentedBookRequest emptySelectedBookDTOReq;

    private final List<Penalty> ALL_PENALTIES = getListOfPenalties(1, 5, 1000, false);

    private final List<Penalty> EMPTY_PENALTIES = new ArrayList<>();

    private List<RentedBookPenalty> penaltiesOfBook;


    /**
     * @param allAvailablePenalties - T???t c??? penalties (ph???i c?? id 1 v?? 2)
     * @param bookId                - id c???a RentedBook
     * @param customer              - Customer
     * @param hasPenalties          - true ??? th??m 2 l???i id (1,2) v??o truy???n
     * @param fee                   - 2 ph?? ph???t t????ng ???ng c???a 2 l???i
     * @return
     */
    private RentedBook mockRentedBook(List<Penalty> allAvailablePenalties, int bookId, Customer customer, boolean hasPenalties, double... fee) {
        // Init all available penalties
        List<Penalty> allPenalties = new ArrayList<>(allAvailablePenalties);
        List<RentedBookPenalty> penaltiesOfBook = new ArrayList<>();

        BookTitle bookTitle = BookTitle.builder()
                .id(bookId + 10)
                .code("BT0" + bookId + 10)
                .titleName("Book " + bookId)
                .build();
        RentedBook book = RentedBook.builder()
                .id(bookId)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false)
                .customer(customer)
                .bookTitle(bookTitle)
                .amount(1500).build();

        if (hasPenalties) {
            RentedBookPenalty p1 = RentedBookPenalty.builder()
                    .id(new RentedBookPenaltyKey(bookId, 1))
                    .rentedBook(book)
                    .penalty(allPenalties.get(0)) // id = 1
                    .fee(fee[0])
                    .build();
            RentedBookPenalty p2 = RentedBookPenalty.builder()
                    .id(new RentedBookPenaltyKey(bookId, 2))
                    .rentedBook(book)
                    .penalty(allPenalties.get(1)) // id = 2
                    .fee(fee[1])
                    .build();

            penaltiesOfBook.add(p1);
            penaltiesOfBook.add(p2);
            book.setPenalties(penaltiesOfBook);
        }

        return book;
    }

    @BeforeEach
    void initData() {
        kw = "Minh";
        customer = Customer.builder()
                .id(1)
                .cccd(1234567899)
                .phone("0987654321")
                .fullName(new FullName(1, "Minh", "Ph???m V??n"))
                .dob(LocalDate.of(2000, 1, 1))
                .address(Address.builder().id(1).city("H?? N???i").district("H?? ????ng").street("Nguy???n Tr??i").build())
                .email("test@mail.com")
                .build();

        RentedBook book1WithPenalties = mockRentedBook(ALL_PENALTIES, 1, customer, true, 500, 700);
        RentedBook book2WithPenalties = mockRentedBook(ALL_PENALTIES, 2, customer, true, 500, 700);
        RentedBook book3NoPenalties = mockRentedBook(ALL_PENALTIES, 3, customer, false, 500, 700);
        RentedBook book4NoPenalties = mockRentedBook(ALL_PENALTIES, 4, customer, false, 500, 700);
        RentedBook book5NoPenalties = mockRentedBook(ALL_PENALTIES, 5, customer, false, 500, 700);

        rentedBooksNoPenalties = new ArrayList<>();
        rentedBooksNoPenalties.add(book3NoPenalties);
        rentedBooksNoPenalties.add(book4NoPenalties);
        rentedBooksNoPenalties.add(book5NoPenalties);
        rentedBooksNoPenaltiesDTOs = RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooksNoPenalties);
        rentedBookNoPenaltiesIds = new ArrayList<>();
        rentedBookNoPenaltiesIds.add(book3NoPenalties.getId());
        rentedBookNoPenaltiesIds.add(book4NoPenalties.getId());
        rentedBookNoPenaltiesIds.add(book5NoPenalties.getId());

        rentedBooksHasPenalties = new ArrayList<>();
        rentedBooksHasPenalties.add(book1WithPenalties);
        rentedBooksHasPenalties.add(book2WithPenalties);
        rentedBooksHasPenalties.add(book3NoPenalties);
        rentedBooksHasPenaltiesDTOs = RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooksHasPenalties);
        rentedBookHasPenaltiesIds = new ArrayList<>();
        rentedBookHasPenaltiesIds.add(book1WithPenalties.getId());
        rentedBookHasPenaltiesIds.add(book2WithPenalties.getId());
        rentedBookHasPenaltiesIds.add(book3NoPenalties.getId());

        selectedBookReqHasPenalties = new ReadyToReturnBooks(rentedBooksHasPenalties, customer.getId());
        selectedBookReqNoPenalties = new ReadyToReturnBooks(rentedBooksNoPenalties, customer.getId());
        noneSelectedBookReq = new ReadyToReturnBooks(null, customer.getId());
        emptySelectedBookReq = new ReadyToReturnBooks(new ArrayList<>(), customer.getId());

        selectedBookDTOReqHasPenalties = new ReturnRentedBookRequest(customer.getId(), RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooksHasPenalties));
        selectedBookDTOReqNoPenalties = new ReturnRentedBookRequest(customer.getId(), RentedBookDTO.rentedBooksToRentedBookDTOs(rentedBooksNoPenalties));
        noneSelectedBookDTOReq = new ReturnRentedBookRequest(customer.getId(), null);
        emptySelectedBookDTOReq = new ReturnRentedBookRequest(customer.getId(), new ArrayList<>());

    }


    /**
     * {@link RentedBookController#getGDRentedBooks(Integer, HttpSession)}: Test Case 1
     *
     * @POSITIVE: KH ???????c ch???n c?? ?????u truy???n ??ang m?????n (3).
     * ??? Tr??? v??? th??ng tin t???t c??? c??c ?????u truy???n ??ang m?????n
     */
    @Test
    void whenGetGDRentedBooks_thenReturn200AndDSTruyen() throws Exception {
        customer.setRentedBooks(new HashSet<>(rentedBooksHasPenalties)); // Tr??nh LAZY load
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        this.mvc.perform(
                        get("/customer/rented-books={id}", customer.getId())
                                .sessionAttr("kwName", kw)) // Mock gi?? tr??? cho session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-truyen-kh"))
                .andExpect(model().attribute("searchKw", kw))
                .andExpect(model().attribute("allRentedBooks", hasSize(3))) // C?? 3 ?????u truy???n m?????n
                .andExpect(model().attribute("allRentedBooks", is(rentedBooksHasPenaltiesDTOs)))
                .andExpect(model().attribute("selectedBooks", noneSelectedBookReq)); // T???i giao di???n n??n ch??a c?? ?????u truy???n n??o ???????c ch???n

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verifyNoMoreInteractions(customerService);
    }


    /**
     * {@link RentedBookController#getGDRentedBooks(Integer, HttpSession)}: Test Case 2
     *
     * @NEGATIVE: KH ???????c ch???n kh??ng c?? ?????u truy???n n??o ??ang m?????n (3).
     * ??? Kh??ng c?? th??ng tin c??c ?????u truy???n ??ang m?????n
     */
    @Test
    void whenGetGDRentedBooks_thenReturn204AndDSTruyenRong() throws Exception {
        customer.setRentedBooks(new HashSet<>()); // Tr??nh LAZY load
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        this.mvc.perform(
                        get("/customer/rented-books={id}", customer.getId())
                                .sessionAttr("kwName", kw)) // Mock gi?? tr??? cho session
                .andExpect(status().isNoContent()) // 204
                .andExpect(view().name("gd-truyen-kh"))
                .andExpect(model().attribute("searchKw", kw))
                .andExpect(model().attribute("allRentedBooks", hasSize(0))); // DS ?????u truy???n m?????n r???ng

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verifyNoMoreInteractions(customerService);
        verifyNoInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDRentedBooks(Integer, HttpSession)}: Test Case 3
     *
     * @NEGATIVE: KH kh??ng t???n t???i
     * ??? 404, th??ng b??o l???i
     */
    @Test
    void whenGetGDRentedBooks_thenReturn404() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenThrow(NotFoundException.class);
        this.mvc.perform(
                        get("/customer/rented-books={id}", customer.getId())
                                .sessionAttr("kwName", kw)) // Mock gi?? tr??? cho session
                .andExpect(status().isNotFound()) // 404
                .andExpect(flash().attribute("errorNoti", notNullValue()));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verifyNoMoreInteractions(customerService);
        verifyNoInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#checkSelectedReturnBooks(Integer, ReadyToReturnBooks, HttpSession, RedirectAttributes)}: Test Case 1
     *
     * @POSITIVE: C?? Truy???n ???????c ch???n ????? tr??? (2)
     * ??? Chuy???n ti???p t???i trang gd-tra-truyen
     */
    @Test
    void whenCheckSelectedReturnBooksAndGetGDTraTruyen_thenRedirectToTraTruyen() throws Exception {
        this.mvc.perform(
                        post("/rented-book/selectToReturn-of={id}", customer.getId())
                                .flashAttr("selectedBooks", selectedBookReqNoPenalties)) // Mock ds ch???n
                // Mong ?????i chuy???n h?????ng
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + customer.getId()));
    }


    /**
     * {@link RentedBookController#checkSelectedReturnBooks(Integer, ReadyToReturnBooks, HttpSession, RedirectAttributes)}: Test Case 2
     *
     * @NEGATIVE: Kh??ng c?? Truy???n n??o ???????c ch???n ????? tr???
     * ??? Chuy???n v??? trang gd-truyen-kh
     */
    @Test
    void whenCheckSelectedReturnBooksAndGetGDTraTruyen_thenRedirectToDSTruyenKH() throws Exception {
        // DS null
        this.mvc.perform(
                        post("/rented-book/selectToReturn-of={id}", customer.getId())
                                .flashAttr("selectedBooks", noneSelectedBookReq)) // Mock ds ch???n
                // Mong ?????i chuy???n h?????ng
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId()));

        // Ds r???ng
        this.mvc.perform(
                        post("/rented-book/selectToReturn-of={id}", customer.getId())
                                .flashAttr("selectedBooks", emptySelectedBookReq)) // Mock ds ch???n
                // Mong ?????i chuy???n h?????ng
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId()));
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 1
     *
     * @POSITIVE: C?? truy???n ???????c ch???n, c?? truy???n t???n t???i, c?? truy???n c?? l???i
     * ??? Hi???n th??? ???????c gd, ds truy???n ???????c ch???n v?? t???n t???i, c??c l???i ??i k??m
     */
    @Test
    void whenGetGDTraTruyen_case1() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        // 3 truy???n ???????c ch???n ????? tr???, c?? l???i
        when(service.getRentedBooksById(rentedBookHasPenaltiesIds)).thenReturn(rentedBooksHasPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", customer.getId())
                                .sessionAttr("selectedBookIds", rentedBookHasPenaltiesIds)) // Mock ids ch???n v??o session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-tra-truyen"))   // ????ng giao di???n
                .andExpect(model().attribute("returnBookReq", is(selectedBookDTOReqHasPenalties)));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).getRentedBooksById(rentedBookHasPenaltiesIds);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 2
     *
     * @POSITIVE: C?? truy???n ???????c ch???n, c?? truy???n t???n t???i, kh??ng truy???n n??o c?? l???i
     * ??? Hi???n th??? ???????c gd, ds truy???n ???????c ch???n v?? t???n t???i
     */
    @Test
    void whenGetGDTraTruyen_case2() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        // Mock 4 penalties
        when(service.getRentedBooksById(rentedBookNoPenaltiesIds)).thenReturn(rentedBooksNoPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", customer.getId())
                                .sessionAttr("selectedBookIds", rentedBookNoPenaltiesIds)) // Mock ds ids ch???n v??o session
                .andExpect(status().isOk())
                .andExpect(view().name("gd-tra-truyen"))   // ????ng giao di???n
                .andExpect(model().attribute("returnBookReq", is(selectedBookDTOReqNoPenalties)));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).getRentedBooksById(rentedBookNoPenaltiesIds);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 3
     *
     * @NEGATIVE: C?? truy???n ???????c ch???n, nh??ng t???t c??? ?????u kh??ng t???n t???i/??ang m?????n
     * ??? Hi???n th??? ???????c gd, ds truy???n ???????c ch???n r???ng
     */
    @Test
    void whenGetGDTraTruyen_case3() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(service.getRentedBooksById(rentedBookNoPenaltiesIds)).thenReturn(emptyRentedBooks); // Nh??ng DS truy???n ???????c tr??? v??? t??? Service l?? r???ng

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", customer.getId())
                                .sessionAttr("selectedBookIds", rentedBookNoPenaltiesIds)) // Mock ds ch???n v??o session
                .andExpect(status().isNoContent())  // 204
                .andExpect(view().name("gd-tra-truyen"))   // Ki???m tra giao di???n
                .andExpect(model().attribute("returnBookReq", is(emptySelectedBookDTOReq)));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).getRentedBooksById(rentedBookNoPenaltiesIds);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 4
     *
     * @NEGATIVE: Kh??ng c?? truy???n ???????c ch???n (Khi NV truy c???p tr???c ti???p b???ng URL)
     * ??? Hi???n th??? ???????c gd, ds truy???n ???????c ch???n r???ng
     */
    @Test
    void whenGetGDTraTruyen_case4() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(service.getRentedBooksById(null)).thenReturn(new ArrayList<>()); // Kh??ng c?? ids
        when(penaltyService.getAllPenalties()).thenReturn(ALL_PENALTIES);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", customer.getId()))
                .andExpect(status().isNoContent())  // 204
                .andExpect(view().name("gd-tra-truyen"))   // Ki???m tra giao di???n
                .andExpect(model().attribute("returnBookReq", is(emptySelectedBookDTOReq)));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).getRentedBooksById(null);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDTraTruyen(Integer, HttpSession)}: Test Case 5
     *
     * @NEGATIVE: KH kh??ng t???n t???i
     * ??? 404, th??ng b??o l???i
     */
    @Test
    void whenGetGDTraTruyen_case5() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenThrow(NotFoundException.class);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", customer.getId()))
                .andExpect(status().isNotFound())  // 404
                .andExpect(flash().attribute("errorNoti", notNullValue()));

        verify(customerService, times(1)).getCustomerById(customer.getId());

        verifyNoMoreInteractions(customerService);
        verifyNoInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 1
     *
     * @POSITIVE: ?????u truy???n t???n t???i, ch??a c?? l???i n??o. >=1 L???i truy???n c?? s???n
     * ??? Hi???n th??? gd-cap-nhat-loi-truyen, l???i truy???n hi???n t???i r???ng (all ids = null)
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case1() throws Exception {
        // Truy???n hi???n t???i ch??a c?? l???i
        RentedBook book = mockRentedBook(ALL_PENALTIES, 99, customer, false);

        List<Penalty> tmpPenalties = getListOfPenalties(1, 5, 1000, true);
        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(penaltyService.getAllPenalties()).thenReturn(ALL_PENALTIES);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // ?????u truy???n m?? 1
                .andExpect(status().isOk())
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong ?????i tr??? v??? gd n??y
                // Ki???m tra object trong form
                .andExpect(model().attribute("rentedBook",
                        hasProperty("customerId", is(expectedDto.getCustomerId()))))
                .andExpect(model().attribute("rentedBook",
                        hasProperty("rentedBookId", is(expectedDto.getRentedBookId()))))
                .andExpect(model().attribute("rentedBook",
                        hasProperty("code", is(expectedDto.getCode()))))
                .andExpect(model().attribute("rentedBook",
                        hasProperty("titleName", is(expectedDto.getTitleName()))))
                .andExpect(model().attribute("rentedBook",
                        hasProperty("penalties", containsInAnyOrder(expectedDto.getPenalties().toArray()))))
                .andExpect(model().attribute("allPenalties", is(ALL_PENALTIES)));

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 2
     *
     * @POSITIVE: ?????u truy???n t???n t???i, ???? c?? l???i truy???n. >=1 L???i truy???n c?? s???n
     * ??? Hi???n th??? gd-cap-nhat-loi-truyen, l???i truy???n hi???n t???i ???????c bao g???m (id != null)
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case2() throws Exception {
        // Truy???n hi???n t???i ???? c?? l???i
        RentedBook book = rentedBooksHasPenalties.get(0);
        List<Penalty> tmpPenalties = rentedBookPenaltiesToPenalties(book.getPenalties(), true); // L???i truy???n c???a truy???n
        tmpPenalties.addAll(getListOfPenalties(3, 5, 1000, true)); // th??m nh???ng l???i c??n thi???u nh??ng set id = null (unchecked)

        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(penaltyService.getAllPenalties()).thenReturn(ALL_PENALTIES);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // ?????u truy???n m?? 1
                .andExpect(status().isOk())
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong ?????i tr??? v??? gd n??y
                .andExpect(model().attribute("rentedBook", is(expectedDto))) // Ki???m tra object trong form
                .andExpect(model().attribute("allPenalties", is(ALL_PENALTIES)));

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)}: Test Case 3
     *
     * @NEGATIVE: ?????u truy???n t???n t???i, nh??ng kh??ng c?? L???i truy???n c?? s???n n???o
     * ??? Hi???n th??? gd-cap-nhat-loi-truyen, http code = 204
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case3() throws Exception {
        RentedBook book = rentedBooksNoPenalties.get(0);
        List<Penalty> tmpPenalties = new ArrayList<>(); // l???i c???a truy???n tr???ng

        RentedBookDTO expectedDto =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(penaltyService.getAllPenalties()).thenReturn(EMPTY_PENALTIES);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", book.getId())) // ?????u truy???n m?? 1
                .andExpect(status().isNoContent()) // 204
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong ?????i tr??? v??? gd n??y
                .andExpect(model().attribute("rentedBook", is(expectedDto))) // Ki???m tra object trong form
                .andExpect(model().attribute("allPenalties", is(EMPTY_PENALTIES)));   // Ki???m tra l???i c?? s???n

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(penaltyService, times(1)).getAllPenalties();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#getGDCapNhatLoiTruyen(Integer)} (Integer)}: Test Case 3
     *
     * @NEGATIVE: ?????u truy???n kh??ng t???n t???i, http code = 404
     * ??? Hi???n th??? gd-cap-nhat-loi-truyen, http code = 404
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_case4() throws Exception {
        int bookId = 404;
        when(service.getRentedBookById(bookId)).thenThrow(NotFoundException.class);

        this.mvc.perform(
                        get("/rented-book/{id}/penalties", bookId)) // ?????u truy???n m?? 1
                .andExpect(status().isNotFound()) // 404
                .andExpect(view().name("gd-cap-nhat-loi-truyen"))  // Mong ?????i tr??? v??? gd n??y
                .andExpect(model().attribute("rentedBook", nullValue())) // Ki???m tra object trong form
                .andExpect(model().attribute("allPenalties", nullValue()));   // Ki???m tra l???i c?? s???n

        verify(service, times(1)).getRentedBookById(bookId);
        verifyNoInteractions(penaltyService);

        verifyNoMoreInteractions(service);
    }


    /**
     * {@link RentedBookController#updatePenaltiesOfRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, Integer)}: Test Case 1
     *
     * @POSITIVE: Truy???n hi???n t???i kh??ng c?? l???i, c?? l???i truy???n m???i ???????c ch???n l??u, kh??ng ph?? n??o b??? b??? tr???ng
     * ??? L??u th??nh c??ng, chuy???n v??? gd-tra-truyen
     */
    @Test
    void whenUpdatePenaltiesOfRentedBook_thenReturn201AndRedirectToGDTraTruyen() throws Exception {
        RentedBook book = mockRentedBook(ALL_PENALTIES, 100, customer, false);

        RentedBook updatedBook = mockRentedBook(ALL_PENALTIES, 100, customer, true, 1500, 2000);
        List<Penalty> newPenalties = rentedBookPenaltiesToPenalties(updatedBook.getPenalties(), false);
        List<Penalty> unselectedPenalties = getListOfPenalties(3, 5, 1000, true);
        List<Penalty> submitPenalties = new ArrayList<>(newPenalties);
        submitPenalties.addAll(unselectedPenalties);

        RentedBookDTO submitBook =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(submitPenalties)
                        .build();


        when(service.getRentedBookById(book.getId())).thenReturn(book);
        for (Penalty p : ALL_PENALTIES) {
            when(penaltyService.getPenaltyById(p.getId())).thenReturn(p);
        }
        when(service.addPenaltiesIntoRentedBook(updatedBook.getPenalties(), new ArrayList<>(), book.getId())).thenReturn(updatedBook);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", submitBook.getRentedBookId())
                                .flashAttr("rentedBook", submitBook)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Tr?????c ???? NV ch???n 3 truy???n ????? tr???
                )
                // Mong ?????i quay lai giao di???n tra-truyen
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + book.getCustomer().getId()));

        verify(service, times(1)).getRentedBookById(submitBook.getRentedBookId());
        for (Penalty p : newPenalties) {
            verify(penaltyService, times(1)).getPenaltyById(p.getId());
        }
        verify(service, times(1)).addPenaltiesIntoRentedBook(anyList(), anyList(), anyInt());

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#updatePenaltiesOfRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, Integer)}: Test Case 2
     *
     * @POSITIVE: Truy???n hi???n t???i ???? c?? c?? l???i, c?? th??m l???i truy???n m???i ???????c ch???n l??u v?? m???t l???i c?? b??? xo??, kh??ng ph?? n??o b??? b??? tr???ng
     * ??? L??u th??nh c??ng, chuy???n v??? gd-tra-truyen
     */
    @Test
    void whenUpdatePenaltiesOfRentedBook_case2() throws Exception {
        // Truy???n hi???n ???? c?? l???i id=1,2
        RentedBook book = mockRentedBook(ALL_PENALTIES, 100, customer, true, 600, 1200);
        List<RentedBookPenaltyKey> idsToRemove = new ArrayList<>();
        idsToRemove.add(RentedBookPenaltyKey.builder().rentedBookId(book.getId()).penaltyId(1).build());

        // C???p nh???t b???ng 4 l???i m???i id = 2,3,4,5 - B??? l???i 1
        RentedBook updatedBook = mockRentedBook(ALL_PENALTIES, 100, customer, false);
        List<RentedBookPenalty> newPenOfBooks = getListOfRentedBookPenalties(updatedBook.getId(), 2,5, 1000);
        updatedBook.setPenalties(newPenOfBooks);
        List<Penalty> newPenalties = getListOfPenalties(2,5, 1000, false);
        List<Penalty> unselectedPenalties = getListOfPenalties(1, 1, 1000, true);
        List<Penalty> submitPenalties = new ArrayList<>(newPenalties);
        submitPenalties.addAll(unselectedPenalties);

        RentedBookDTO submitBook =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(submitPenalties)
                        .build();


        when(service.getRentedBookById(book.getId())).thenReturn(book);
        for (Penalty p : ALL_PENALTIES) {
            when(penaltyService.getPenaltyById(p.getId())).thenReturn(p);
        }
        when(service.addPenaltiesIntoRentedBook(updatedBook.getPenalties(), idsToRemove, book.getId())).thenReturn(updatedBook);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", book.getId())
                                .flashAttr("rentedBook", submitBook)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Tr?????c ???? NV ch???n 3 truy???n ????? tr???
                )
                // Mong ?????i quay lai giao di???n tra-truyen
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + book.getCustomer().getId()));

        verify(service, times(1)).getRentedBookById(book.getId());
        for (Penalty p : newPenalties) {
            verify(penaltyService, times(1)).getPenaltyById(p.getId());
        }
        verify(service, times(1)).addPenaltiesIntoRentedBook(anyList(), anyList(), anyInt());

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#updatePenaltiesOfRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, Integer)}: Test Case 3
     *
     * @POSITIVE: Kh??ng c?? l???i truy???n n??o ???????c ch???n l??u
     * ??? L??u th??nh c??ng, chuy???n v??? gd-tra-truyen
     */
    @Test
    void whenUpdatePenaltiesOfRentedBook_case3() throws Exception {
        // Truy???n hi???n t???i c?? 2 l???i
        RentedBook book = mockRentedBook(ALL_PENALTIES, 100, customer, true, 1500, 2000);
        List<RentedBookPenaltyKey> idsToRemove = getKeys(book.getPenalties());

        // Update l???i kh??ng c?? l???i
        RentedBook updatedBook = mockRentedBook(ALL_PENALTIES, 100, customer, false);
        List<Penalty> submitPenalties = getListOfPenalties(1, 5, 1000, true);
        RentedBookDTO submitBook =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(submitPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(service.addPenaltiesIntoRentedBook(new ArrayList<>(), idsToRemove, book.getId())).thenReturn(updatedBook);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", submitBook.getRentedBookId())
                                .flashAttr("rentedBook", submitBook)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Tr?????c ???? NV ch???n 3 truy???n ????? tr???
                )
                // Mong ?????i quay lai giao di???n tra-truyen
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rented-book/selected-of=" + book.getCustomer().getId()));

        verify(service, times(1)).getRentedBookById(book.getId());
        verifyNoInteractions(penaltyService);
        verify(service, times(1)).addPenaltiesIntoRentedBook(new ArrayList<>(), idsToRemove, book.getId());

        verifyNoMoreInteractions(service);
    }


    /**
     * {@link RentedBookController#updatePenaltiesOfRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, Integer)}: Test Case 4
     *
     * @POSITIVE: Ph?? ph???t kh??ng h???p l???
     * ??? L??u kh??ng th??nh c??ng, ??? l???i trang cap-nhat-loi-truyen
     */
    @Test
    void whenUpdatePenaltiesOfRentedBook_case4() throws Exception {
        // Truy???n hi???n t???i c?? 2 l???i
        RentedBook book = mockRentedBook(ALL_PENALTIES, 100, customer, true, 1500, 2000);
        List<RentedBookPenaltyKey> idsToRemove = getKeys(book.getPenalties());

        // Update l???i kh??ng c?? l???i
        RentedBook updatedBook = mockRentedBook(ALL_PENALTIES, 100, customer, false);
        List<Penalty> submitPenalties = getListOfPenalties(1, 5, 1000, true);
        submitPenalties.get(0).setRecommendedFee(200); // M???t l???i < 500 ?????ng
        RentedBookDTO submitBook =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(submitPenalties)
                        .build();

        when(service.getRentedBookById(book.getId())).thenReturn(book);
        when(service.addPenaltiesIntoRentedBook(new ArrayList<>(), idsToRemove, book.getId())).thenReturn(updatedBook);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", submitBook.getRentedBookId())
                                .flashAttr("rentedBook", submitBook)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Tr?????c ???? NV ch???n 3 truy???n ????? tr???
                )
                // Mong ?????i ??? lai giao di???n cap-nhat-loi-truyen
                .andExpect(status().isBadRequest())
                .andExpect(redirectedUrl("/rented-book/" + book.getId() + "/penalties"));

        verifyNoInteractions(service);
        verifyNoInteractions(penaltyService);
    }


    /**
     * {@link RentedBookController#updatePenaltiesOfRentedBook(RentedBookDTO, BindingResult, RedirectAttributes, Integer)}: Test Case 5
     *
     * @POSITIVE: L???i khi c???p nh???t l???i truy???n cho RentedBook
     * ??? 304 - L??u kh??ng th??nh c??ng, ??? l???i trang cap-nhat-loi-truyen
     */
    @Test
    void whenUpdatePenaltiesOfRentedBook_case5() throws Exception {
        // Truy???n hi???n t???i c?? 2 l???i
        RentedBook book = mockRentedBook(ALL_PENALTIES, 100, customer, true, 1500, 2000);
        List<RentedBookPenaltyKey> idsToRemove = getKeys(book.getPenalties());

        // Update l???i b???ng 2 l???i kh??c
        List<Penalty> uncheckedPenalties = getListOfPenalties(1, 3, 1000, true);
        List<Penalty> checkedPenalties = getListOfPenalties(3, 5, 1000, false);
        List<RentedBookPenalty> newPenOfBooks = getListOfRentedBookPenalties(book.getId(), 3, 5, 1000);
        List<Penalty> submitPenalties = new ArrayList<>(uncheckedPenalties);
        submitPenalties.addAll(checkedPenalties);
        RentedBookDTO submitBook =
                RentedBookDTO.builder()
                        .customerId(book.getCustomer().getId())
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(submitPenalties)
                        .build();


        when(service.getRentedBookById(book.getId())).thenReturn(book);
        for (Penalty p : ALL_PENALTIES) {
            when(penaltyService.getPenaltyById(p.getId())).thenReturn(p);
        }
        when(service.addPenaltiesIntoRentedBook(anyList(), anyList(), anyInt())).thenThrow(FailedToResetBookPenaltiesException.class);

        this.mvc.perform(
                        post("/rented-book/{id}/save-penalties", submitBook.getRentedBookId())
                                .flashAttr("rentedBook", submitBook)
                                .sessionAttr("selectedBookIds", getListOfInts(1, 3)) // Tr?????c ???? NV ch???n 3 truy???n ????? tr???
                )
                // Mong ?????i quay lai giao di???n tra-truyen
                .andExpect(status().isNotModified())
                .andExpect(redirectedUrl("/rented-book/" + book.getId() + "/penalties"));

        verify(service, times(1)).getRentedBookById(book.getId());
        for (Penalty p : checkedPenalties) {
            verify(penaltyService, times(1)).getPenaltyById(p.getId());
        }
        verify(service, times(1)).addPenaltiesIntoRentedBook(anyList(), anyList(), anyInt());

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(penaltyService);
    }


    //=============================================== C??c h??m t???o mock data ===============================================

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
                                .name("L???i")
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
                        .name("L???i")
                        .description("")
                        .recommendedFee(fee)
                        .build()
                ).collect(Collectors.toList());
    }

    private List<Penalty> rentedBookPenaltiesToPenalties(List<RentedBookPenalty> rentedBookPenalties, boolean changeOrigin) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentedBookPenalties != null) {
            for (RentedBookPenalty r : rentedBookPenalties) {
                Penalty p = changeOrigin ? r.getPenalty() : Penalty.builder()
                        .id(r.getPenalty().getId())
                        .name(r.getPenalty().getName())
                        .description(r.getPenalty().getDescription())
                        .build();
                p.setRecommendedFee(r.getFee());
                penalties.add(p);
            }
        }
        return penalties;
    }

    private List<RentedBookPenaltyKey> getKeys(List<RentedBookPenalty> penaltiesOfBook) {
        return penaltiesOfBook.stream().map(RentedBookPenalty::getId).collect(Collectors.toList());
    }

}
