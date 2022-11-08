package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.request.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.model.request.RentedBookDTO;
import com.ptit.thuetruyenmgmt.model.request.ReturnRentedBookRequest;
import com.ptit.thuetruyenmgmt.service.BillService;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BillController {

    @Autowired
    private BillService service;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RentedBookService rentedBookService;


    /**
     * Kiểm tra thông tin cần thiết để tạo Bill rồi gọi hiển thị trang `xac-nhan-thanh-toan`.
     *
     * @param customerId
     * @param session
     * @param redirect
     * @return
     */
    @PostMapping("/customer/check-bill")
    public ModelAndView checkAndGenerateBillInfo(@RequestParam("customerId") int customerId,
                                                 HttpSession session,
                                                 RedirectAttributes redirect) {

        List<Integer> ids = (List<Integer>) session.getAttribute("selectedBookIds");
        if (ids == null || ids.isEmpty()) {
            redirect.addFlashAttribute("invalidBill",
                    "Tạo thanh toán thất bại!!! Không có đầu truyện nào được chọn.");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }

        ModelAndView mav = new ModelAndView("redirect:/customer/show-bill?customerId=" + customerId);
        return mav;
    }


    /**
     * Hiển thị giao diện xác nhận thanh toán.
     *
     * @param customerId
     * @param session
     * @param redirect
     * @return
     */
    @GetMapping("/customer/show-bill")
    public ModelAndView generateBillInfo(@RequestParam("customerId") int customerId,
                                         HttpSession session,
                                         RedirectAttributes redirect) {
        // Xử lý tạo thông tin hoá đơn
        Customer customer = customerService.getCustomerById(customerId);
        List<Integer> bookIds = (List<Integer>) session.getAttribute("selectedBookIds");

        List<RentedBook> rentedBooks = rentedBookService.getRentedBooksById(bookIds);
        Bill billInfo = service.createBillInfo(rentedBooks, 1);

        ModelAndView mav = new ModelAndView("xac-nhan-thanh-toan");
        mav.addObject("customerId", customerId);
        mav.addObject("customerInfo", customer);
        mav.addObject("paySuccess", false);
        mav.addObject("bill", billInfo);

        return mav;
    }

    @PostMapping("/customer/create-bill")
    public ModelAndView payBill() {
        return null;
    }

}
