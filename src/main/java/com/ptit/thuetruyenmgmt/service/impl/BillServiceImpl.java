package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.repository.BillRepository;
import com.ptit.thuetruyenmgmt.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Override
    public Bill createBillInfo(Bill bill) {
        return null;
    }

    @Override
    public boolean saveBillInfo(Bill bill) {
        return false;
    }
}
