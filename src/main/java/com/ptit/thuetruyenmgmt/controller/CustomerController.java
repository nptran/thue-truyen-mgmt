package com.ptit.thuetruyenmgmt.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;

    // Gọi trang giao diện search hiển thị
    @GetMapping(path = {"/customer"})
    public String index() {
        return "search";
    }

    @GetMapping("/customer/search")
    public ModelAndView searchCustomer(@RequestParam(name = "kw-name", defaultValue = "", required = false) String customerName,
                                       HttpSession session) {
        ModelAndView mav = new ModelAndView("search");
        List<Customer> foundCustomers = service.getCustomerByName(customerName);
        mav.addObject("customers", foundCustomers);
        session.setAttribute("kw-name", customerName);

        return mav;
    }

//    @PostMapping("/customer/create")
//    public String save(@Valid Customer customer, BindingResult result, RedirectAttributes redirect) {
//        service.createCustomer(customer);
//        redirect.addFlashAttribute("success", "Saved customer successfully!");
//        return "redirect:/customer";
//    }

}
