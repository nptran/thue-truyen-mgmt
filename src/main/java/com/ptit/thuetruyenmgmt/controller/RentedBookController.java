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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RentedBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);

    @Autowired
    private RentedBookService service;

    @Autowired
    private PenaltyService penaltyService;


    /**
     * Tới giao diện hiển thị tất cả các RentedBook hiện tại của Khách trên `ds-truyen-kh`.
     *
     * @param id
     * @return
     */
    @GetMapping("/customer/rented-books={id}")
    public ModelAndView getRentedBooks(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("ds-truyen-kh");
        List<RentedBook> allRentedBooks = service.getRentedBooksByCustomer(id);
        ReadyToReturnBooks wrapper = new ReadyToReturnBooks();
        wrapper.setCustomerId(id);
        mav.addObject("selectedBooks", wrapper);
        mav.addObject("allRentedBooks", allRentedBooks);
        return mav;
    }

    /**
     * Kiểm tra các truyện được chọn trong giao diện `ds-truyen-kh` và
     * chuyển sang giao diện `tra-truyen` nếu có ít nhất một Đầu truyện được chọn trả
     *
     * @param customerId
     * @param selectedBooks DS truyện muốn trả đã chọn, được gửi từ form trong Giao diện `ds-truyen-kh`
     * @param redirect
     * @return `tra-truyen`
     */
    @PostMapping("/rented-book/selectToReturn-of={id}")
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
        session.setAttribute("selectedBooks", selectedBooks);  // Lưu lại trong session để xử lý ở các page sau

        List<RentedBookDTO> rentedBookDtos = rentedBooksToRentedBookDTOs(books);

        // Dựng giao diện tra-truyen
        redirect.addFlashAttribute("selectedBooks", selectedBooks); // Truyền ds truyện đang chọn sang dưới dạng attribute
        ModelAndView mav = new ModelAndView("tra-truyen");

        mav.addObject("returnBookDtos", new ReturnRentedBookRequest(customerId, rentedBookDtos));
        mav.addObject("allPenalties", penaltyService.getAllPenalties());

        return mav;
    }


    /**
     * Hiển thị Giao diện để cập nhật Lỗi truyện cho Đầu truyện.
     * Tạo các đối tượng cần để khởi tạo cho Form
     *
     * @param bookId
     * @param redirect
     * @return `cap-nhat-loi-truyen`
     */
    @RequestMapping("/rented-book/{id}/penalties")
    public ModelAndView getGDCapNhatLoiTruyen(@PathVariable(name = "id") int bookId,
                                              HttpSession session,
                                              RedirectAttributes redirect) {
        ModelAndView mav = new ModelAndView("cap-nhat-loi-truyen");

        RentedBook book = service.getRentedBookById(bookId);
        List<Penalty> currentPenalties = service.rentedBookPenaltiesToPenalties(book.getPenalties());
        List<Integer> currentPenaltiesIds = currentPenalties.stream().map(Penalty::getId).collect(Collectors.toList());
        List<Penalty> allAvailablePenalties = penaltyService.getAllPenalties();
        List<Penalty> tmpPenalties = new ArrayList<>();
        // Đặt lại ds penalty để chỉ tick những ô penalty hiện tại của book
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
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();

//        mav.addObject("returnableBookIds", selectedBookIds);
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
    public ModelAndView saveRentedBook(@Valid RentedBookDTO rentedBook,
                                       BindingResult result,
                                       RedirectAttributes redirect,
                                       HttpSession session,
                                       @PathVariable(name = "id") int rentedBookId) {

        LOGGER.debug(rentedBook.toString());
        if (result.hasErrors()) {
            redirect.addFlashAttribute("failedMessage", "Dữ liệu trong form không hợp lệ");
            return new ModelAndView("redirect:/rented-book/" + rentedBookId + "/penalties");
        }
        RentedBook originalRentedBook = service.getRentedBookById(rentedBookId);
        List<RentedBookPenalty> currPenalties = originalRentedBook.getPenalties();

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

        // Nếu lưu thành công
//        redirect.addFlashAttribute("savePenaltiesSuccess", "Cập nhật lỗi truyện thành công!");
        ModelAndView mav = new ModelAndView("tra-truyen");
        mav.addObject("savePenaltiesSuccess", "Cập nhật lỗi truyện thành công!");
        ReadyToReturnBooks selectedBooks = (ReadyToReturnBooks) session.getAttribute("selectedBooks");
        mav.addObject("returnBookDtos",
                new ReturnRentedBookRequest(rentedBook.getCustomerId(),
                        rentedBooksToRentedBookDTOs(selectedBooks.getWillBeReturnedBooks())));
        mav.addObject("allPenalties", penaltyService.getAllPenalties());
        return mav;
    }


    private List<RentedBookDTO> rentedBooksToRentedBookDTOs(List<RentedBook> rentedBooks) {

        List<RentedBookDTO> rentedBookDtos = new ArrayList<>();
        for (RentedBook book : rentedBooks) {
            RentedBook originalBook = service.getRentedBookById(book.getId());
            List<Penalty> currentPenalties = service.rentedBookPenaltiesToPenalties(originalBook.getPenalties());

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
