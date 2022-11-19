package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.model.request.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.request.ReturnRentedBookRequest;
import com.ptit.thuetruyenmgmt.model.request.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RentedBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);

    private RentedBookService service;

    private PenaltyService penaltyService;

    @Autowired
    public RentedBookController(RentedBookService rentedBookService, PenaltyService penaltyService) {
        this.service = rentedBookService;
        this.penaltyService = penaltyService;
    }


    /**
     * Tải trang `gd-truyen-kh` để hiển thị tất cả các RentedBook hiện tại của một Khách hàng
     *
     * @param id id khách hàng
     * @return
     */
    @GetMapping("/customer/rented-books={id}")
    public ModelAndView getGDRentedBooks(@PathVariable Integer id, HttpSession session) {
        ModelAndView mav = new ModelAndView("gd-truyen-kh");
        List<RentedBook> allRentedBooks = service.getRentedBooksByCustomer(id);
        ReadyToReturnBooks wrapper = new ReadyToReturnBooks();
        wrapper.setCustomerId(id);
        String searchKw = (String) session.getAttribute("kwName");
        mav.addObject("searchKw", searchKw);
        mav.addObject("selectedBooks", wrapper);
        mav.addObject("allRentedBooks", allRentedBooks);
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
    public ModelAndView checkSelectedReturnBooksAndGetGDTraTruyen(@PathVariable("id") Integer customerId,
                                                                  @ModelAttribute("selectedBooks") ReadyToReturnBooks selectedBooks,
                                                                  HttpSession session,
                                                                  RedirectAttributes redirect) {
        List<RentedBook> books = selectedBooks.getWillBeReturnedBooks();
        if (books.isEmpty()) {
            redirect.addFlashAttribute("returnNothing", "Hãy chọn ít nhất 1 đầu truyện để trả!");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }
        List<Integer> selectedBookIds = books.stream().map(RentedBook::getId).collect(Collectors.toList());

        // Lưu lại trong session để xử lý ở các page sau
        session.setAttribute("selectedBookIds", selectedBookIds);

        // Chuyển hướng sang dựng giao diện tra-truyen
        ModelAndView mav = new ModelAndView("redirect:/rented-book/selected-of=" + customerId);

        return mav;
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
                                       HttpSession session) {
        // Lấy ra danh sách các truyện đã chọn từ session
        List<Integer> ids = (List<Integer>) session.getAttribute("selectedBookIds");
        List<RentedBook> selectedBooks = service.getRentedBooksById(ids);

        // Dựng giao diện tra-truyen
        ModelAndView mav = new ModelAndView("gd-tra-truyen");

        List<RentedBookDTO> selectedBookDtos = rentedBooksToRentedBookDTOs(selectedBooks);
        mav.addObject("returnBookReq", new ReturnRentedBookRequest(customerId, selectedBookDtos));
        mav.addObject("allPenalties", penaltyService.getAllPenalties());

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
        ModelAndView mav = new ModelAndView("gd-cap-nhat-loi-truyen");

        RentedBook originalRentedBook = service.getRentedBookById(bookId);
        List<Penalty> currentPenalties = service.rentedBookPenaltiesToPenalties(originalRentedBook.getPenalties());
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
    public ModelAndView saveRentedBook(@Valid @ModelAttribute("rentedBook") RentedBookDTO rentedBook,
                                       BindingResult result,
                                       RedirectAttributes redirect,
                                       HttpSession session,
                                       @PathVariable(name = "id") Integer rentedBookId) {

        LOGGER.debug(rentedBook.toString());
        if (result.hasErrors()) {
            redirect.addFlashAttribute("failedMessage", "Dữ liệu trong form không hợp lệ");
            return new ModelAndView("redirect:/rented-book/" + rentedBookId + "/penalties");
        }
        RentedBook originalRentedBook = service.getRentedBookById(rentedBookId);
        List<RentedBookPenalty> currPenalties = originalRentedBook.getPenalties() == null ? new ArrayList<>() : originalRentedBook.getPenalties(); // Nếu chưa có lỗi thì gán tạm list rỗng

        List<Penalty> selectedPenalties = rentedBook.getPenalties();
        selectedPenalties.removeIf(penalty -> penalty.getId() == null); // Remove NULL penalties (no selected)
        List<Integer> newPenIds = selectedPenalties.stream().map(Penalty::getId).collect(Collectors.toList());

        List<RentedBookPenaltyKey> removedIds = new ArrayList<>();
        for (RentedBookPenalty p : currPenalties) {
            int id = p.getId().getPenaltyId();
            if (!newPenIds.contains(id)) {
                removedIds.add(p.getId());
            }
        }

        List<RentedBookPenalty> penalties = new ArrayList<>();
        for (Penalty penalty : selectedPenalties) {
            Penalty originalPenalty = penaltyService.getPenaltyById(penalty.getId());
            double fee = penalty.getRecommendedFee();
            RentedBookPenalty rentedBookPenalty =
                    RentedBookPenalty.builder()
                            .id(new RentedBookPenaltyKey(originalRentedBook.getId(), originalPenalty.getId()))
                            .rentedBook(originalRentedBook)
                            .penalty(originalPenalty)
                            .fee(fee)
                            .build();
            penalties.add(rentedBookPenalty);
        }

        // Cập nhật lại RentedBookPenalty
        try {
            service.addPenaltiesIntoRentedBook(penalties, removedIds, originalRentedBook.getId());
        } catch (Exception e) {
            redirect.addFlashAttribute("failedMessage", e.getMessage());
            return new ModelAndView("redirect:/rented-book/" + rentedBookId + "/penalties");
        }


        List<Integer> selectedBooks = (List<Integer>) session.getAttribute("selectedBookIds");
        List<RentedBook> selectedRentedBooks = service.getRentedBooksById(selectedBooks);

        // Nếu lưu thành công chuyển hướng về trang Trả truyện
        redirect.addFlashAttribute("savePenaltiesSuccess", "Cập nhật lỗi truyện thành công!");
        ModelAndView mav = new ModelAndView("redirect:/rented-book/selected-of=" + originalRentedBook.getCustomer().getId());
        return mav;
    }


    private List<RentedBookDTO> rentedBooksToRentedBookDTOs(List<RentedBook> rentedBooks) {

        List<RentedBookDTO> rentedBookDtos = new ArrayList<>();
        for (RentedBook book : rentedBooks) {
            RentedBook originalBook = service.getRentedBookById(book.getId());
            List<Penalty> currentPenalties = originalBook.getPenalties() == null ? new ArrayList<>() : service.rentedBookPenaltiesToPenalties(originalBook.getPenalties());

            RentedBookDTO pobDto = RentedBookDTO.builder()
                    .rentedBookId(originalBook.getId())
                    .code(originalBook.getBookTitle().getCode())
                    .titleName(originalBook.getBookTitle().getTitleName())
                    .rentedTime(originalBook.getRentedTime())
                    .amount(originalBook.getAmount())
                    .penalties(currentPenalties)
                    .build();
            rentedBookDtos.add(pobDto);
        }

        return rentedBookDtos;
    }

}
