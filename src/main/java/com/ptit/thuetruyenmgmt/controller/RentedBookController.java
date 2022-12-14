package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.exception.FailedToResetBookPenaltiesException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.model.dto.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.dto.ReturnRentedBookRequest;
import com.ptit.thuetruyenmgmt.model.dto.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RentedBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);

    private RentedBookService service;

    private PenaltyService penaltyService;

    private CustomerService customerService;

    @Autowired
    public RentedBookController(RentedBookService rentedBookService, PenaltyService penaltyService, CustomerService customerService) {
        this.service = rentedBookService;
        this.penaltyService = penaltyService;
        this.customerService = customerService;
    }


    /**
     * Tải trang `gd-truyen-kh` để hiển thị tất cả các RentedBook hiện tại của một Khách hàng
     *
     * @param id id khách hàng
     * @return
     */
    @GetMapping("/customer/rented-books={id}")
    public ModelAndView getGDRentedBooks(@PathVariable Integer id, HttpSession session, RedirectAttributes redirect) {
        session.setAttribute("customerId", id);
        Customer customer;
        try {
            customer = customerService.getCustomerById(id);
        } catch (NotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/customer");
            mav.setStatus(HttpStatus.NOT_FOUND);
            redirect.addFlashAttribute("errorNoti", "Khách hàng không tồn tại!");
            return mav;
        }

        List<RentedBook> allRentedBooks = new ArrayList<>(customer.getRentedBooks());
        allRentedBooks.removeIf(RentedBook::isPaid);
        ModelAndView mav = new ModelAndView("gd-truyen-kh");
        if (allRentedBooks.isEmpty()) mav.setStatus(HttpStatus.NO_CONTENT);
        String searchKw = (String) session.getAttribute("kwName");
        List<RentedBookDTO> allRentedBooksInfo = RentedBookDTO.rentedBooksToRentedBookDTOs(allRentedBooks);
        ReadyToReturnBooks wrapper = new ReadyToReturnBooks();
        wrapper.setCustomerId(id);
        mav.addObject("searchKw", searchKw);
        mav.addObject("selectedBooks", wrapper);
        mav.addObject("allRentedBooks", allRentedBooksInfo);
        mav.addObject("customerInfo", customer);
        return mav;
    }

    /**
     * Kiểm tra các truyện được chọn trong giao diện `gd-truyen-kh` sau đó
     * lưu DS này vào session. (nếu có ít nhất một Đầu truyện được chọn trả)
     * Cuối cùng, gọi để dựng giao diện `tra-truyen`
     *
     * @param customerId
     * @param selectedBooks DS truyện muốn trả đã chọn, được gửi từ form trong Giao diện `gd-truyen-kh`
     * @param redirect
     * @return `tra-truyen`
     */
    @PostMapping(value = "/rented-book/selectToReturn-of={id}")
    public ModelAndView checkSelectedReturnBooks(@PathVariable("id") Integer customerId,
                                                 @ModelAttribute("selectedBooks") ReadyToReturnBooks selectedBooks,
                                                 HttpSession session,
                                                 RedirectAttributes redirect) {
        List<RentedBook> books = selectedBooks.getWillBeReturnedBooks();
        if (books == null || books.isEmpty()) {
            redirect.addFlashAttribute("returnNothing", "Hãy chọn ít nhất 1 đầu truyện để trả!");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }
        List<Integer> selectedBookIds = books.stream().map(RentedBook::getId).collect(Collectors.toList());

        // Lưu lại trong session để xử lý ở các page sau
        session.setAttribute("selectedBookIds", selectedBookIds);

        // Chuyển hướng sang dựng giao diện tra-truyen
        return new ModelAndView("redirect:/rented-book/selected-of=" + customerId);
    }


    /**
     * Tải trang `tra-truyen`
     *
     * @param customerId
     * @param session
     * @return
     */
    @GetMapping(value = "/rented-book/selected-of={id}")
    public ModelAndView getGDTraTruyen(@PathVariable("id") Integer customerId,
                                       HttpSession session, RedirectAttributes redirect) {
        Customer customer;
        try {
            customer = customerService.getCustomerById(customerId);
        } catch (NotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/customer");
            mav.setStatus(HttpStatus.NOT_FOUND);
            redirect.addFlashAttribute("errorNoti", "Khách hàng không tồn tại!");
            return mav;
        }

        // Lấy ra danh sách các truyện đã chọn từ session
        List<Integer> ids = (List<Integer>) session.getAttribute("selectedBookIds");
        List<RentedBook> selectedBooks = service.getRentedBooksById(ids);
        List<RentedBookDTO> selectedBookDtos = RentedBookDTO.rentedBooksToRentedBookDTOs(selectedBooks);
        ReturnRentedBookRequest req = new ReturnRentedBookRequest(customerId, selectedBookDtos);

        // Dựng giao diện tra-truyen
        ModelAndView mav = new ModelAndView("gd-tra-truyen");
        if (selectedBooks.isEmpty()) mav.setStatus(HttpStatus.NO_CONTENT);
        mav.addObject("returnBookReq", req);
        mav.addObject("customerInfo", customer);

        return mav;
    }


    /**
     * Hiển thị Giao diện để cập nhật Lỗi truyện cho Đầu truyện.
     * Tạo các đối tượng cần để khởi tạo cho Form
     *
     * @return `cap-nhat-loi-truyen`
     */
    @RequestMapping("/rented-book/{id}/penalties")
    public ModelAndView getGDCapNhatLoiTruyen(@PathVariable(name = "id") Integer bookId) {
        RentedBook originalRentedBook;
        try {
            originalRentedBook = service.getRentedBookById(bookId);
        } catch (NotFoundException e) {
            ModelAndView mav = new ModelAndView("gd-cap-nhat-loi-truyen");
            mav.setStatus(HttpStatus.NOT_FOUND);
            return mav;
        }
        List<Penalty> currentPenalties = RentedBookDTO.rentedBookPenaltiesToPenalties(originalRentedBook.getPenalties());
        List<Integer> currentPenaltiesIds = currentPenalties.stream().map(Penalty::getId).collect(Collectors.toList());
        List<Penalty> allAvailablePenalties = penaltyService.getAllPenalties();

        List<Penalty> tmpPenalties = new ArrayList<>();
        // Đặt lại ds penalty để chỉ tick những ô penalty hiện tại của originalRentedBook
        for (Penalty originalPenalty : allAvailablePenalties) {
            Integer id = null;
            if (currentPenaltiesIds.contains(originalPenalty.getId())) {
                id = originalPenalty.getId();
            }
            Penalty p = Penalty.builder()
                    .id(id)
                    .name(originalPenalty.getName())
                    .description(originalPenalty.getDescription())
                    .recommendedFee(originalPenalty.getRecommendedFee())
                    .build();
            tmpPenalties.add(p);
        }

        // Build object để set form cập nhật lỗi truyện
        RentedBookDTO rentedBookDTO =
                RentedBookDTO.builder()
                        .customerId(originalRentedBook.getCustomer().getId())
                        .rentedBookId(originalRentedBook.getId())
                        .code(originalRentedBook.getBookTitle().getCode())
                        .titleName(originalRentedBook.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

        ModelAndView mav = new ModelAndView("gd-cap-nhat-loi-truyen");
        if (allAvailablePenalties.isEmpty()) mav.setStatus(HttpStatus.NO_CONTENT);
        mav.addObject("rentedBook", rentedBookDTO);
        mav.addObject("allPenalties", allAvailablePenalties);
        return mav;
    }

    /**
     * Xử lý lấy dữ liệu từ form và cập nhật DS Lỗi truyện cho Đầu truyện.
     *
     * @param rentedBook
     * @param result
     * @param redirect
     * @return `tra-truyen` - nếu cập nhật thành công
     */
    @PostMapping("/rented-book/{id}/save-penalties")
    public ModelAndView updatePenaltiesOfRentedBook(@Valid @ModelAttribute("rentedBook") RentedBookDTO rentedBook,
                                       BindingResult result,
                                       RedirectAttributes redirect,
                                       @PathVariable(name = "id") Integer rentedBookId) {
        LOGGER.debug(rentedBook.toString());
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("redirect:/rented-book/" + rentedBookId + "/penalties");
            mav.setStatus(HttpStatus.BAD_REQUEST);
            redirect.addFlashAttribute("failedMessage", result.getAllErrors().get(0).getDefaultMessage());
            return mav;
        }
        RentedBook originalRentedBook = service.getRentedBookById(rentedBookId);
        List<RentedBookPenalty> currPenalties = originalRentedBook.getPenalties() == null ? new ArrayList<>() : originalRentedBook.getPenalties(); // Nếu chưa có lỗi thì gán tạm list rỗng

        List<Penalty> selectedPenalties = rentedBook.getPenalties();
        selectedPenalties.removeIf(penalty -> penalty.getId() == null); // Remove NULL penalties (no selected)
        List<Integer> newPenIds = selectedPenalties.stream().map(Penalty::getId).collect(Collectors.toList());

        // Lấy ID các Penalty hiện tại mà không còn nằm trong các Penalty mới
        List<RentedBookPenaltyKey> removedIds = new ArrayList<>();
        for (RentedBookPenalty p : currPenalties) {
            int id = p.getId().getPenaltyId();
            if (!newPenIds.contains(id)) {
                removedIds.add(p.getId());
            }
        }

        // lấy thông tin các Penalty mới
        List<RentedBookPenalty> penalties = new ArrayList<>();
        for (Penalty penalty : selectedPenalties) {
            Penalty originalPenalty = penaltyService.getPenaltyById(penalty.getId());
            RentedBookPenalty rentedBookPenalty =
                    RentedBookPenalty.builder()
                            .id(new RentedBookPenaltyKey(originalRentedBook.getId(), originalPenalty.getId()))
                            .rentedBook(originalRentedBook)
                            .penalty(originalPenalty)
                            .fee(penalty.getRecommendedFee())
                            .build();
            penalties.add(rentedBookPenalty);
        }

        // Cập nhật lại RentedBookPenalty
        try {
            service.addPenaltiesIntoRentedBook(penalties, removedIds, originalRentedBook.getId());
        } catch (FailedToResetBookPenaltiesException | NotFoundException e) {
            ModelAndView mav = new ModelAndView("redirect:/rented-book/" + rentedBookId + "/penalties");
            redirect.addFlashAttribute("failedMessage", e.getMessage());
            mav.setStatus(HttpStatus.NOT_MODIFIED);
            return mav;
        }

        // Nếu lưu thành công chuyển hướng về trang Trả truyện
        redirect.addFlashAttribute("savePenaltiesSuccess", "Cập nhật lỗi truyện thành công!");
        return new ModelAndView("redirect:/rented-book/selected-of=" + originalRentedBook.getCustomer().getId());
    }

}
