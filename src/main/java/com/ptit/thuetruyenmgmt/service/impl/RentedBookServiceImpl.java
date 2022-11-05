package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentedBookServiceImpl implements RentedBookService {

    @Autowired
    private RentedBookRepository repository;

    @Autowired
    private RentedBookPenaltyRepository rentedBookPenaltyRepository;


}
