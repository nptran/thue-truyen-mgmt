package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.service.BillService;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
public class BillControllerTest {

    @MockBean
    private BillService service;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private RentedBookService rentedBookService;

    @Autowired
    private MockMvc mvc;


}
