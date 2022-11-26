package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.service.BillService;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class BillController {

    private BillService service;

    private CustomerService customerService;

    private RentedBookService rentedBookService;

    @Autowired
    public BillController(BillService billService, CustomerService customerService, RentedBookService rentedBookService) {
        this.service = billService;
        this.customerService = customerService;
        this.rentedBookService = rentedBookService;
    }


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
            redirect.addFlashAttribute("returnNothing",
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
        Customer customer;
        try {
            customer = customerService.getCustomerById(customerId);
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("errorNoti", "Khách hàng không tồn tại. Vui lòng thử lại!");
            return new ModelAndView("redirect:/customer");
        }

        // Kiểm tra đầu truyện đã chọn
        List<Integer> bookIds = (List<Integer>) session.getAttribute("selectedBookIds");
        if (bookIds == null || bookIds.isEmpty()) {
            redirect.addFlashAttribute("returnNothing",
                    "Tạo thanh toán thất bại!!! Không có đầu truyện nào được chọn.");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }

        List<RentedBook> rentedBooks = rentedBookService.getRentedBooksById(bookIds);
        if (rentedBooks.isEmpty()) {
            redirect.addFlashAttribute("returnNothing",
                    "Tạo thanh toán thất bại!!! Không có đầu truyện nào được chọn.");
            return new ModelAndView("redirect:/customer/rented-books=" + customerId);
        }

        ModelAndView mav;
        try {
            Bill billInfo = service.createPayInfo(rentedBooks, 1);
            mav = new ModelAndView("gd-xac-nhan-thanh-toan");
            mav.addObject("customerId", customerId);
            mav.addObject("customerInfo", customer);
            mav.addObject("paySuccess", false);
            mav.addObject("bill", billInfo);
        } catch (NotFoundException e) {
            mav = new ModelAndView("gd-chinh-nv");
            mav.setStatus(HttpStatus.UNAUTHORIZED);
        }

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
        Customer customer;
        try {
            customer = customerService.getCustomerById(customerId);
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("errorNoti", "Khách hàng không tồn tại. Vui lòng thử lại!");
            return new ModelAndView("redirect:/customer");
        }

        try {
            service.saveBillInfo(bill);
        } catch (Exception e) {
            redirect.addFlashAttribute("returnNothing",
                    "Tạo thanh toán thất bại!!!");
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
    public ModelAndView notifySuccessStatus(@RequestParam("customerId") Integer customerId,
                                            @RequestParam(name = "paid") boolean paidStatus,
                                            Bill bill) {
        if (bill.getRentedBooks() == null) { // Check đúng luồng được gửi từ payBill
            return new ModelAndView("redirect:/customer");
        }
        ModelAndView mav = new ModelAndView("gd-xac-nhan-thanh-toan");
        mav.addObject("customerId", customerId);
        return mav;
    }

}
