package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.request.ReadyToReturnBooks;
import com.ptit.thuetruyenmgmt.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BillController {

    @Autowired
    private BillService service;

    @GetMapping("/customer/show-bill")
    public ModelAndView generateBillInfo(@RequestParam("customerId") int customerId,
            @Valid ReadyToReturnBooks beingReturnBooksWrapper,
                                           BindingResult result, RedirectAttributes redirect) {
        ModelAndView mav = new ModelAndView("tra-truyen");
        return mav;
    }

}
