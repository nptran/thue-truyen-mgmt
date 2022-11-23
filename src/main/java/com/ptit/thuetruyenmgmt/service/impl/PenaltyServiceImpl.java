package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.repository.PenaltyRepository;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PenaltyServiceImpl implements PenaltyService {

    @Autowired
    private PenaltyRepository repository;

    private static final String RESOURCE_NAME = Customer.class.getSimpleName();

    @Override
    public List<Penalty> getAllPenalties() {
        List<Penalty> p = repository.findAll();
        return p;
    }

    @Override
    public Penalty getPenaltyById(int id) {
        Optional<Penalty> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(RESOURCE_NAME);
        }

        return optional.get();
    }
}
