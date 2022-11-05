package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.model.Staff;
import com.ptit.thuetruyenmgmt.repository.StaffRepository;
import com.ptit.thuetruyenmgmt.repository.UserRepository;
import com.ptit.thuetruyenmgmt.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository repository;

    @Override
    public Staff login(String code) {
        return repository.findByCode(code);
    }

}
