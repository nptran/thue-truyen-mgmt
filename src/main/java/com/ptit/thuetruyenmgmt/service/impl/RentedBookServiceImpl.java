package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentedBookServiceImpl implements RentedBookService {

    @Autowired
    private RentedBookRepository repository;

    @Autowired
    private RentedBookPenaltyRepository rentedBookPenaltyRepository;


    @Override
    public List<RentedBook> getRentedBooksByCustomer(int customerId) {
        return repository.findAllByCustomer_IdAndIsPaidIsFalse(customerId);
    }

    @Override
    public RentedBook addPenaltiesIntoRentedBook(List<RentedBookPenalty> penalties, int id) {
        return null;
    }

    @Override
    public boolean updateRentStatus(List<Integer> payBookIds) {
        return false;
    }
}
