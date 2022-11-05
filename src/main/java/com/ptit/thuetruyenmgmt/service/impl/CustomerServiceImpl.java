package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;


    @Override
    public Customer getCustomerById(int id) {
        return null;
    }

    @Override
    public List<Customer> getCustomerByName(String name) {
        return null;
    }
}
