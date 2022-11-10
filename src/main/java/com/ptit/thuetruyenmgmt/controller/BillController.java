package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.service.BillService;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
    public ModelAndView checkAndGenerateBillInfo(@RequestParam("customerId") Integer customerId,
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
    @GetMapping(value = "/customer/show-bill", params = {"!paid"})
    public ModelAndView generateBillInfo(@RequestParam("customerId") Integer customerId,
                                         HttpSession session,
                                         RedirectAttributes redirect) {
        // Xử lý tạo thông tin hoá đơn
        Customer customer = customerService.getCustomerById(customerId);
        List<Integer> bookIds = (List<Integer>) session.getAttribute("selectedBookIds");

        List<RentedBook> rentedBooks = rentedBookService.getRentedBooksById(bookIds);
        Bill billInfo = service.createPayInfo(rentedBooks, 1);

        ModelAndView mav = new ModelAndView("gd-xac-nhan-thanh-toan");
        mav.addObject("customerId", customerId);
        mav.addObject("customerInfo", customer);
        mav.addObject("paySuccess", false);
        mav.addObject("bill", billInfo);

        return mav;
    }


    /**
     * Thực hiện thanh toán sau đó gọi để hiển thị kết quả.
     *
     * @param customerId
     * @param bill
     * @param session
     * @param redirect
     * @return
     */
    @PostMapping("/customer/create-bill")
    public ModelAndView payBill(@RequestParam("customerId") Integer customerId,
                                @Valid Bill bill,
                                HttpSession session,
                                RedirectAttributes redirect) {
        Customer customer = customerService.getCustomerById(customerId);
        if (!service.saveBillInfo(bill)) {
            redirect.addFlashAttribute("invalidBill",
                    "Tạo thanh toán thất bại!!! Không có đầu truyện nào được chọn.");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }

        // Reset lại Đầu truyện chọn để trả
        session.removeAttribute("selectedBookIds");
        redirect.addFlashAttribute("bill", bill);
        redirect.addFlashAttribute("customerInfo", customer);
        redirect.addFlashAttribute("paySuccess", true);
        return new ModelAndView("redirect:/customer/show-bill?customerId=" + customerId + "&paid=true");
    }


    /**
     * Hiển thị Thông báo kết quả nếu thanh toán Thành công.
     *
     * @param customerId
     * @return Hiện thông báo thành công để quay lại trang chủ.
     */
    @GetMapping(value = "/customer/show-bill", params = {"paid"})
    public ModelAndView notifySuccessStatus(@RequestParam("customerId") Integer customerId) {

        ModelAndView mav = new ModelAndView("gd-xac-nhan-thanh-toan");
        mav.addObject("customerId", customerId);

        return mav;
    }

}
