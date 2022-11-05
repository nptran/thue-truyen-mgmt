package com.ptit.thuetruyenmgmt.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;

    // Gọi trang giao diện search hiển thị
    @GetMapping(path = {"/customer"})
    public String index() {
//        model.addAttribute("customers", studentService.getAll());
        return "search";
    }

    @GetMapping("/customer/search")
    public String searchCustomer(@RequestParam("kw-name") String customerName, Model model) {
        model.addAttribute("customers", service.getCustomerByName(customerName));
        return "search";
    }

    @PostMapping("/customer/create")
    public String save(@Valid Customer customer, BindingResult result, RedirectAttributes redirect) {
        service.createCustomer(customer);
        redirect.addFlashAttribute("success", "Saved customer successfully!");
        return "redirect:/customer";
    }

}
