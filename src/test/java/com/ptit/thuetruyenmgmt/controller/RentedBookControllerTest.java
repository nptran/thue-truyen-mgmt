package com.ptit.thuetruyenmgmt.controller;

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
     * @POSITIVE: Khi NV chọn KH id 1 từ GD tìm kiếm tên Minh và KH đang có 3 truyện cần mượn.
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
                                .sessionAttr("kw-name", kw)) // Mock giá trị cho session
                .andExpect(status().isOk())
                .andExpect(view().name("ds-truyen-kh"))
                .andExpect(model().attribute("searchKw", kw))
                .andExpect(model().attribute("allRentedBooks", hasSize(3))) // Có 3 đầu truyện mượn
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b1.getId()))
//                                hasProperty("fullName", Matchers.hasToString(c1.getFullName().toString()))
                        )
                )))
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b2.getId()))
//                                hasProperty("fullName", Matchers.hasToString(c1.getFullName().toString()))
                        )
                )))
                .andExpect(model().attribute("allRentedBooks", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(b3.getId()))
//                                hasProperty("fullName", Matchers.hasToString(c2.getFullName().toString()))
                        )
                )))
                .andExpect(model().attribute("selectedBooks", selected));

        verify(service, times(1)).getRentedBooksByCustomer(c1.getId());
        verifyNoMoreInteractions(service);
    }

    /**
     * @POSITIVE: Khi NV chọn 2 truyện và nhấn trả truyện
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
     * @POSITIVE: Sau khi hệ thống kiểm tra có truyện được chọn, hiển thị tra-truyen
     */
    @Test
    void whenGetGDTraTruyen_thenReturn200AndTraTruyenView() throws Exception {
        // 2 truyện được chọn để trả
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
        List<Penalty> allPenalties = getListOfPenalties(1, 4, 1000);
        when(service.getRentedBooksById(ids)).thenReturn(selectedBooks);
        when(service.getRentedBookById(1)).thenReturn(b1);
        when(service.getRentedBookById(2)).thenReturn(b2);
        when(penaltyService.getAllPenalties()).thenReturn(allPenalties);

        this.mvc.perform(
                        get("/rented-book/selected-of={id}", c1.getId())
                                .sessionAttr("selectedBookIds", ids)) // Mock ds chọn vào session
                .andExpect(status().isOk())
                .andExpect(view().name("tra-truyen"))
                .andExpect(model().attribute("returnBookReq", instanceOf(ReturnRentedBookRequest.class)))
                .andExpect(model().attribute("allPenalties", is(allPenalties)));

        verify(service, times(1)).getRentedBooksById(ids);
        verify(penaltyService, times(1)).getAllPenalties();
    }


    /**
     * @POSITIVE: Khi NV nhấn nút Cập nhật lỗi truyện trên giao diện tra-truyen cho 1 Đầu truyện
     */
    @Test
    void whenGetGDCapNhatLoiTruyen_thenReturn200AndGDTraTruyen() throws Exception {
        // Truyện hiện tại chưa có lỗi
        RentedBook book = RentedBook.builder()
                .id(1)
                .customer(Customer.builder().id(1).build())
                .bookTitle(BookTitle.builder().code("").titleName("").build())
                .build();

        List<Penalty> allAvailablePenalties = getListOfPenalties(1, 5, 1000);
        List<Penalty> tmpPenalties = getListOfPenalties(1, 5, 1000);
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
                .andExpect(view().name("cap-nhat-loi-truyen"))  // Mong đợi trả về gd này
                .andExpect(model().attribute("rentedBook", is(expectedDto)))
                .andExpect(model().attribute("allPenalties", is(allAvailablePenalties)));

        verify(service, times(1)).getRentedBookById(book.getId());
        verify(service, times(1)).rentedBookPenaltiesToPenalties(book.getPenalties());
        verify(penaltyService, times(1)).getAllPenalties();
    }


    /**
     * @POSITIVE: Khi NV chọn 2 lỗi truyện, nhập phí 1000 và nhấn Lưu
     */
    @Test
    void whenSaveRentedBook_thenReturn201AndRedirectToGDTraTruyen() throws Exception {
        // NV chọn 2 lỗi truyện với phí 1000
        List<Penalty> selectedPenalties = getListOfPenalties(1, 2, 1000);
        // Mock 2 Penalty gốc tương ứng với phí phạt 500
        List<Penalty> originalPenalties = getListOfPenalties(1, 2, 500);

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

    private List<Penalty> getListOfPenalties(int fromPenID, int toPenID, double fee) {
        return IntStream.range(fromPenID, toPenID + 1)
                .mapToObj(i -> Penalty.builder()
                        .id(i)
                        .name("Lỗi")
                        .description("")
                        .recommendedFee(fee)
                        .build()
                ).collect(Collectors.toList());
    }

    private List<RentedBookDTO> rentedBooksToRentedBookDTOs(List<RentedBook> rentedBooks) {
        List<RentedBookDTO> dtos = new ArrayList<>();
        for (RentedBook book : rentedBooks) {
            RentedBookDTO dto =
                    RentedBookDTO.builder()
                            .rentedBookId(book.getId())
                            .code(book.getBookTitle().getCode())
                            .titleName(book.getBookTitle().getTitleName())
                            .rentedTime(book.getRentedTime())
                            .amount(book.getAmount())
                            .penalties(new ArrayList<>())
                            .build();
            dtos.add(dto);
        }

        return dtos;
    }

}
