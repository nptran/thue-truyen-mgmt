package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RentedBookController {

    @Autowired
    private RentedBookService service;
    @RequestMapping(path= "/customer/rented-books")
    public String getRentedBooksOfCustomer() {
        return "ds-truyen-kh";
    }

    @GetMapping("/customer/rented-books={id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("rentedBooks", service.getRentedBooksByCustomer(id));
        return "ds-truyen-kh";
    }

}
