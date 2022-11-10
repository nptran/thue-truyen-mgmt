package com.ptit.thuetruyenmgmt.controller;

import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.FullName;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
//import org.mockito.BDDMockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService service;

    @Autowired
    private MockMvc mvc;


    @Test
    public void whenGetGDSearch_thenReturnStatus200AndSearchView() throws Exception {
        this.mvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }


    @Test
    public void whenSearchCustomer_thenReturnStatus200AndCustomers() throws Exception {
        String kw = "Minh";
        // Giả định service trả về 2 KH tên Minh
        Customer c1 = Customer.builder().id(1).fullName(new FullName(1, "Minh", "Phạm")).build();
        Customer c2 = Customer.builder().id(2).fullName(new FullName(2, "Minh", "Trần")).build();
        List<Customer> customers = new ArrayList<>();
        customers.add(c1);
        customers.add(c2);

        when(service.getCustomerByName(kw)).thenReturn(customers);
        this.mvc.perform(get("/customer/search?kw-name=" + kw))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("customers", hasSize(2)))
                .andExpect(model().attribute("customers", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(1)),
                                hasProperty("fullName", Matchers.hasToString(c1.getFullName().toString()))
                        )
                )))
                .andExpect(model().attribute("customers", hasItem(
                        allOf(
                                hasProperty("id", Matchers.is(2)),
                                hasProperty("fullName", Matchers.hasToString(c2.getFullName().toString()))
                        )
                )));

        verify(service, times(1)).getCustomerByName(kw);
        verifyNoMoreInteractions(service);
    }
}
