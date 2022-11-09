package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer getCustomerById(int id);

    List<Customer> getCustomerByName(String name);

//    Customer createCustomer(Customer customer);

}
