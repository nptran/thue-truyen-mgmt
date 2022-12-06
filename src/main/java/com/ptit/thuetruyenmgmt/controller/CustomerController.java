package com.ptit.thuetruyenmgmt.controller;

import javax.servlet.http.HttpSession;

import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CustomerController {

    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Gọi trang giao diện search hiển thị
    @GetMapping(path = {"/customer"})
    public ModelAndView getGDSearch() {
        ModelAndView mav = new ModelAndView("gd-tim-kh");
        return mav;
    }

    @GetMapping("/customer/search")
    public ModelAndView searchCustomer(@RequestParam(name = "kw-name", defaultValue = "", required = false) String customerName,
                                       HttpSession session) {
        List<Customer> foundCustomers = service.getCustomerByName(customerName);

        ModelAndView mav = new ModelAndView("gd-tim-kh");
        mav.addObject("kwName", customerName);
        session.setAttribute("kwName", customerName);
        mav.addObject("customers", foundCustomers);

        return mav;
    }

//    @PostMapping("/customer/create")
//    public String save(@Valid Customer customer, BindingResult result, RedirectAttributes redirect) {
//        service.createCustomer(customer);
//        redirect.addFlashAttribute("success", "Saved customer successfully!");
//        return "redirect:/customer";
//    }

}
