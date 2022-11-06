package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RentedBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentedBookController.class);

    @Autowired
    private RentedBookService service;

    @Autowired
    private PenaltyService penaltyService;


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

    @PostMapping("/customer/check-return")
    public ModelAndView checkSelectedReturnBooks(@RequestParam("customerId") int customerId,
                                                 ReadyToReturnBooks selectedBooks,
                                                 RedirectAttributes redirect) {
        List<RentedBook> books = selectedBooks.getWillBeReturnedBooks();
        if (books.isEmpty()) {
            redirect.addFlashAttribute("returnNothing", "Hãy chọn ít nhất 1 đầu truyện để trả!");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }

        List<RentedBookDTO> req = new ArrayList<>();
        for (RentedBook book : books) {
            RentedBook originalBook = service.getRentedBookById(book.getId());
            List<Penalty> penalties = service.rentedBookPenaltiesToPenalties(originalBook.getPenalties());
            RentedBookDTO pobDto = RentedBookDTO.builder()
                    .rentedBookId(originalBook.getId())
                    .code(originalBook.getBookTitle().getCode())
                    .titleName(originalBook.getBookTitle().getTitleName())
                    .rentedTime(originalBook.getRentedTime())
                    .amount(originalBook.getAmount())
                    .penalties(penalties)
                    .build();
            req.add(pobDto);
        }

        return proceedReturnBooks(customerId, new ReturnRentedBookRequest(customerId, req), redirect);
    }

    @RequestMapping("/rented-book/{id}/penalties")
    public ModelAndView updatePenaltyOfRentedBook(@PathVariable(name = "id") int bookId, RedirectAttributes redirect) {
        ModelAndView mav = new ModelAndView("cap-nhat-loi-truyen");
        RentedBook book = service.getRentedBookById(bookId);
        List<Penalty> currentPenalties = service.rentedBookPenaltiesToPenalties(book.getPenalties());
        List<Integer> currentPenaltiesIds = currentPenalties.stream().map(Penalty::getId).collect(Collectors.toList());
        List<Penalty> allAvailablePenalties = penaltyService.getAllPenalties();
        List<Penalty> tmpPenalties = new ArrayList<>(allAvailablePenalties);

        // Đặt lại ds penalty để tránh bị lệch index khi hiển thị trên view
        for (Penalty p : tmpPenalties) {
            if (!currentPenaltiesIds.contains(p.getId())) {
                p.setId(null);
            }
        }

        RentedBookDTO rentedBookDTO =
                RentedBookDTO.builder()
                        .rentedBookId(book.getId())
                        .code(book.getBookTitle().getCode())
                        .titleName(book.getBookTitle().getTitleName())
                        .penalties(tmpPenalties)
                        .build();


        mav.addObject("rentedBook", rentedBookDTO);
        mav.addObject("allPenalties", allAvailablePenalties);
        return mav;
    }

    @PostMapping("rented-book/save")
    public ModelAndView saveRentedBook(@Valid RentedBookDTO rentedBookDto, BindingResult result, RedirectAttributes redirect) {
        ModelAndView mav = new ModelAndView("home");
        LOGGER.debug(rentedBookDto.toString());
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/rented-book/" + rentedBookDto.getRentedBookId() + "/penalties");
        }
        RentedBook rentedBook = service.getRentedBookById(rentedBookDto.getRentedBookId());
        List<RentedBookPenalty> penalties = new ArrayList<>();
        List<Penalty> selectedPenalties = rentedBookDto.getPenalties();
        selectedPenalties.removeIf(penalty -> penalty.getId() == null); // Remove NULL penalties (no selected)

        for (Penalty penalty : selectedPenalties) {
            Penalty originalPenalty = penaltyService.getPenaltyById(penalty.getId());
            double fee = penalty.getRecommendedFee();
            RentedBookPenalty rentedBookPenalty =
                    RentedBookPenalty.builder()
                            .rentedBook(rentedBook)
                            .penalty(originalPenalty)
                            .fee(fee)
                            .build();
            penalties.add(rentedBookPenalty);
        }

        service.addPenaltiesIntoRentedBook(penalties, rentedBook.getId());

        return mav;
    }

    @GetMapping("/customer/returningOfCustomer={id}")
    public ModelAndView proceedReturnBooks(@PathVariable(name = "id") Integer customerId,
                                           @ModelAttribute ReturnRentedBookRequest returnRentedBookReq,
                                           RedirectAttributes redirect) {
        ModelAndView mav = new ModelAndView("tra-truyen");
        List<RentedBookDTO> rentedBookDtos = returnRentedBookReq.getRentedBookDtos();
        for (RentedBookDTO dto : rentedBookDtos) {
            for (Penalty p : dto.getPenalties()) {
                LOGGER.info("PENALTY: " + p);
            }
        }

//        List<Penalty> allPenalties = penaltyService.getAllPenalties();
//        for (RentedBook book : books) {
//            List<RentedBookPenaltyRequest> pobRequests = new ArrayList<>();
//            for (Penalty penalty : allPenalties) {
//                RentedBookPenaltyRequest possiblePenalty =
//                        RentedBookPenaltyRequest.builder()
//                                .bookId(book.getId())
//                                .penaltyId(penalty.getId())
//                                .fee(penalty.getRecommendedFee())
//                                .build();
//                pobRequests.add(possiblePenalty);
//            }
//            book.setPenalties(pobRequests);
//        }

//        mav.addObject("availablePenalties", penaltiesOfBook);
        mav.addObject("returnBookDtos", returnRentedBookReq);
        mav.addObject("allPenalties", penaltyService.getAllPenalties());

        return mav;
    }

}
