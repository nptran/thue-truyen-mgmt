package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.exception.FailedToPayException;
import com.ptit.thuetruyenmgmt.exception.NoneSelectedBookToReturnException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.service.BillService;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillController.class)
public class BillControllerTest {

    @MockBean
    private BillService service;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private RentedBookService rentedBookService;

    @Autowired
    private MockMvc mvc;

    private List<Integer> selectedBookIds;

    private List<Integer> emptySelectedBookIds = new ArrayList<>();

    private Customer customer;

    private List<RentedBook> selectedBooks;

    private List<RentedBook> emptySelectedBooks = new ArrayList<>();

    private List<Penalty> allPenalties;

    private List<RentedBookPenalty> penaltiesOfBook;

    private Bill billInfo;

    private Staff staff;

    @BeforeEach
    public void initData() {
        // Init selected book ids
        selectedBookIds = new ArrayList<>();
        selectedBookIds.add(1);
        selectedBookIds.add(2);
        selectedBookIds.add(3);

        // Init customer
        customer = Customer.builder()
                .id(1)
                .cccd(1234567899)
                .phone("0987654321")
                .fullName(new FullName(1, "Tr???n", "Test"))
                .dob(LocalDate.of(2000, 1, 1))
                .address(Address.builder().id(1).city("H?? N???i").district("H?? ????ng").street("Nguy???n Tr??i").build())
                .email("test@mail.com")
                .build();

        // Init all available penalties
        allPenalties = getListOfPenalties(0, 5, 1000, false);


        // Init selected books
        selectedBooks = new ArrayList<>();

        BookTitle bookTitle1 = BookTitle.builder()
                .id(10)
                .code("BT010")
                .titleName("Book 1")
                .build();
        RentedBook b1 = RentedBook.builder()
                .id(1)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false)
                .customer(customer)
                .bookTitle(bookTitle1)
                .amount(1500).build();
        RentedBookPenalty p1 = RentedBookPenalty.builder()
                .id(new RentedBookPenaltyKey(1, 1))
                .rentedBook(b1)
                .penalty(allPenalties.get(1))
                .fee(500)
                .build();
        RentedBookPenalty p2 = RentedBookPenalty.builder()
                .id(new RentedBookPenaltyKey(1, 2))
                .rentedBook(b1)
                .penalty(allPenalties.get(2))
                .fee(700)
                .build();
        penaltiesOfBook = new ArrayList<>();
        penaltiesOfBook.add(p1);
        penaltiesOfBook.add(p2);
        b1.setPenalties(penaltiesOfBook);

        BookTitle bookTitle2 = BookTitle.builder()
                .id(20)
                .code("BT020")
                .titleName("Book 2")
                .build();
        RentedBook b2 = RentedBook.builder()
                .id(1)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false)
                .customer(customer)
                .bookTitle(bookTitle2)
                .amount(2000).build();

        BookTitle bookTitle3 = BookTitle.builder()
                .id(30)
                .code("BT030")
                .titleName("Book 3")
                .build();
        RentedBook b3 = RentedBook.builder()
                .id(3)
                .rentedTime(LocalDateTime.of(2022, 10, 10, 10, 10, 10))
                .isPaid(false)
                .customer(customer)
                .bookTitle(bookTitle3)
                .amount(3000).build();

        selectedBooks.add(b1);
        selectedBooks.add(b2);
        selectedBooks.add(b3);

        // Init staff
        staff = Staff.staffBuilder()
                .id(1)
                .fullName(new FullName(10, "Nh??n", "Vi??n"))
                .loginCode("NV010")
                .position(StaffPosition.THU_NGAN)
                .build();

        // Init Bill information
        billInfo = Bill.builder()
                .createTime(LocalDateTime.of(2022, 10, 15, 10, 10, 10))
                .totalAmount(50000)
                .rentedBooks(selectedBooks)
                .staff(staff)
                .build();
    }


    /**
     * {@link BillController#checkAndGenerateBillInfo(Integer, HttpSession, RedirectAttributes)}: Test case 1
     *
     * @POSITIVE: C?? >=1 ?????u truy???n ???? ???????c ch???n
     */
    @Test
    public void whenCheckAndGenerateBillInfoCase1() throws Exception {
        this.mvc.perform(post("/customer/check-bill")
                        .param("customerId", customer.getId().toString())
                        .sessionAttr("selectedBookIds", selectedBookIds)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/show-bill?customerId=" + customer.getId())); // Mong ?????i ??i???u h?????ng
    }


    /**
     * @NEGATIVE: Kh??ng c?? ?????u truy???n n??o ???? ???????c ch???n
     */
    @Test
    public void whenCheckAndGenerateBillInfoCase2() throws Exception {
        // C?? trong session nh??ng empty
        this.mvc.perform(post("/customer/check-bill")
                        .param("customerId", customer.getId().toString())
                        .sessionAttr("selectedBookIds", emptySelectedBookIds)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("returnNothing", notNullValue())) // Mong ?????i m???t th??ng b??o l???i
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId())); // Mong ?????i ??i???u h?????ng

        // Kh??ng c?? trong session (null)
        this.mvc.perform(post("/customer/check-bill")
                        .param("customerId", customer.getId().toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("returnNothing", notNullValue())) // Mong ?????i m???t th??ng b??o l???i
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId())); // Mong ?????i ??i???u h?????ng
    }


    /**
     * @POSITIVE: KH t???n t???i, >=1 ?????u truy???n ???? ch???n t???n t???i, NV
     * t???n t???i
     */
    @Test
    public void whenGenerateBillInfoCase1() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(rentedBookService.getRentedBooksById(selectedBookIds)).thenReturn(selectedBooks);
        when(service.createPayInfo(selectedBooks, staff.getId())).thenReturn(billInfo);

        this.mvc.perform(get("/customer/show-bill")
                        .sessionAttr("selectedBookIds", selectedBookIds)
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("gd-xac-nhan-thanh-toan"))
                .andExpect(model().attribute("customerId", is(customer.getId())))
                .andExpect(model().attribute("customerInfo", is(customer)))
                .andExpect(model().attribute("paySuccess", is(false)))
                .andExpect(model().attribute("bill", is(billInfo)));

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(rentedBookService, times(1)).getRentedBooksById(selectedBookIds);
        verify(service, times(1)).createPayInfo(selectedBooks, staff.getId());

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rentedBookService);
        verifyNoMoreInteractions(service);
    }


    /**
     * @NEGATIVE: KH t???n t???i, >=1 ?????u truy???n ???? ch???n t???n t???i, NV
     * kh??ng t???n t???i
     */
    @Test
    public void whenGenerateBillInfoCase2() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(rentedBookService.getRentedBooksById(selectedBookIds)).thenReturn(selectedBooks);
        when(service.createPayInfo(selectedBooks, staff.getId())).thenThrow(NotFoundException.class);

        this.mvc.perform(get("/customer/show-bill")
                        .sessionAttr("selectedBookIds", selectedBookIds)
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().isUnauthorized());

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(rentedBookService, times(1)).getRentedBooksById(selectedBookIds);
        verify(service, times(1)).createPayInfo(selectedBooks, staff.getId());

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rentedBookService);
        verifyNoMoreInteractions(service);
    }


    /**
     * @NEGATIVE: KH t???n t???i, >=1 truy???n ???????c ch???n nh??ng kh??ng truy???n
     * n??o t???n t???i
     */
    @Test
    public void whenGenerateBillInfoCase3() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(rentedBookService.getRentedBooksById(selectedBookIds)).thenReturn(emptySelectedBooks);

        this.mvc.perform(get("/customer/show-bill")
                        .sessionAttr("selectedBookIds", selectedBookIds)
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId())) // Mong ?????i chuy???n h?????ng
                .andExpect(flash().attribute("returnNothing", notNullValue())) // Mong ?????i th??ng b??o l???i
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(rentedBookService, times(1)).getRentedBooksById(selectedBookIds);
        verifyNoInteractions(service);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(rentedBookService);
    }


    /**
     * @NEGATIVE: KH t???n t???i, kh??ng c?? truy???n ???????c ch???n
     */
    @Test
    public void whenGenerateBillInfoCase4() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        // Empty selectedBookIds
        this.mvc.perform(get("/customer/show-bill")
                        .sessionAttr("selectedBookIds", emptySelectedBookIds)
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId())) // Mong ?????i chuy???n h?????ng
                .andExpect(flash().attribute("returnNothing", notNullValue())) // Mong ?????i th??ng b??o l???i
        ;

        // Null selectedBookIds
        this.mvc.perform(get("/customer/show-bill")
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId())) // Mong ?????i chuy???n h?????ng
                .andExpect(flash().attribute("returnNothing", notNullValue())) // Mong ?????i th??ng b??o l???i
        ;

        verify(customerService, times(2)).getCustomerById(customer.getId());
        verifyNoInteractions(rentedBookService);
        verifyNoInteractions(service);

        verifyNoMoreInteractions(customerService);
    }


    /**
     * @NEGATIVE: KH kh??ng t???n t???i
     */
    @Test
    public void whenGenerateBillInfoCase5() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenThrow(NotFoundException.class);

        // Empty selectedBookIds
        this.mvc.perform(get("/customer/show-bill")
                        .sessionAttr("selectedBookIds", emptySelectedBookIds)
                        .param("customerId", customer.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer")) // Mong ?????i chuy???n h?????ng
                .andExpect(flash().attribute("errorNoti", notNullValue())) // Mong ?????i th??ng b??o l???i
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verifyNoInteractions(rentedBookService);
        verifyNoInteractions(service);

        verifyNoMoreInteractions(customerService);
    }


    /**
     * @POSITIVE: KH t???n t???i, >=1 ?????u truy???n ???? ch???n t???n t???i
     */
    @Test
    public void whenPayBillCase1() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(service.saveBillInfo(billInfo)).thenReturn(true);

        this.mvc.perform(post("/customer/create-bill")
                        .param("customerId", customer.getId().toString())
                        .flashAttr("bill", billInfo)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/show-bill?customerId=" + customer.getId() + "&paid=true"))
                .andExpect(flash().attribute("bill", billInfo))
                .andExpect(flash().attribute("customerInfo", is(customer)))
                .andExpect(flash().attribute("paySuccess", true))
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).saveBillInfo(billInfo);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
    }


    /**
     * @NEGATIVE: KH t???n t???i, >=1 ?????u truy???n ???? ch???n t???n t???i, thanh to??n b???
     * l???i
     */
    @Test
    public void whenPayBillCase2() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(service.saveBillInfo(billInfo)).thenThrow(FailedToPayException.class);

        this.mvc.perform(post("/customer/create-bill")
                        .param("customerId", customer.getId().toString())
                        .flashAttr("bill", billInfo)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId()))
                .andExpect(flash().attribute("returnNothing", notNullValue()))
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).saveBillInfo(billInfo);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
    }


    /**
     * @NEGATIVE: KH t???n t???i, ch??a ch???n ho???c kh??ng ?????u truy???n
     * n??o t???n t???i
     */
    @Test
    public void whenPayBillCase3() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);
        when(service.saveBillInfo(billInfo)).thenThrow(NoneSelectedBookToReturnException.class);

        this.mvc.perform(post("/customer/create-bill")
                        .param("customerId", customer.getId().toString())
                        .flashAttr("bill", billInfo)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer/rented-books=" + customer.getId()))
                .andExpect(flash().attribute("returnNothing", notNullValue()))
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verify(service, times(1)).saveBillInfo(billInfo);

        verifyNoMoreInteractions(customerService);
        verifyNoMoreInteractions(service);
    }


    /**
     * @NEGATIVE: KH kh??ng t???n t???i
     */
    @Test
    public void whenPayBillCase4() throws Exception {
        when(customerService.getCustomerById(customer.getId())).thenThrow(NotFoundException.class);

        this.mvc.perform(post("/customer/create-bill")
                        .param("customerId", customer.getId().toString())
                        .flashAttr("bill", billInfo)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"))
                .andExpect(flash().attribute("errorNoti", notNullValue()))
        ;

        verify(customerService, times(1)).getCustomerById(customer.getId());
        verifyNoInteractions(service);

        verifyNoMoreInteractions(customerService);
    }


    /**
     * @POSITIVE: Thanh to??n ???????c th???c hi???n ????ng
     */
    @Test
    public void whenNotifySuccessStatusCase1() throws Exception {
        this.mvc.perform(get("/customer/show-bill")
                        .param("customerId", customer.getId().toString())
                        .param("paid", "true")
                        .flashAttr("bill", billInfo)
                        .flashAttr("customerInfo", customer)
                        .flashAttr("paySuccess", true)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("gd-xac-nhan-thanh-toan"))
                .andExpect(model().attribute("customerId", customer.getId()))
        ;
    }


    /**
     * @NEGATIVE: NV t???i l???i ho???c truy c???p tr???c ti???p URL
     */
    @Test
    public void whenNotifySuccessStatusCase2() throws Exception {
        this.mvc.perform(get("/customer/show-bill")
                        .param("customerId", customer.getId().toString())
                        .param("paid", "true")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"))
        ;
    }


    // ================================= Mock functions =================================

    private List<Penalty> getListOfPenalties(int fromPenID, int toPenID, double fee, boolean idNull) {
        return IntStream.range(fromPenID, toPenID + 1)
                .mapToObj(i -> Penalty.builder()
                        .id(idNull ? null : i)
                        .name("L???i")
                        .description("M?? t??? l???i")
                        .recommendedFee(fee)
                        .build()
                ).collect(Collectors.toList());
    }

}
