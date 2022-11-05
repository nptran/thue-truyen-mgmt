package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.repository.PenaltyRepository;
import com.ptit.thuetruyenmgmt.service.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaltyServiceImpl implements PenaltyService {

    @Autowired
    private PenaltyRepository repository;


    @Override
    public List<Penalty> getAllPenalties() {
        return repository.findAll();
    }
}
