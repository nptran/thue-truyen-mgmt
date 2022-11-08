package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.request.RentedBookDTO;
import com.ptit.thuetruyenmgmt.repository.BillRepository;
import com.ptit.thuetruyenmgmt.repository.CustomerRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.repository.StaffRepository;
import com.ptit.thuetruyenmgmt.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentedBookRepository rentedBookRepository;

    @Autowired
    private StaffRepository staffRepository;


    private static final String STAFF_RESOURCE = Staff.class.getSimpleName();


    @Override
    @Transactional
    public Bill createBillInfo(List<RentedBook> rentedBooks, Integer staffId) {
        Optional<Staff> staffOptional = staffRepository.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new NotFoundException(STAFF_RESOURCE);
        }
        Staff staff = staffOptional.get();


        LocalDateTime now = LocalDateTime.now();
        double totalAmount = 0;
        for (RentedBook book : rentedBooks) {
            totalAmount += calculateTotalAmount(book, now);
        }

        return Bill.builder()
                .createTime(now)
                .totalAmount(totalAmount)
                .rentedBooks(rentedBooks)
                .staff(staff)
                .build();
    }

    @Override
    public boolean saveBillInfo(Bill bill) {
        return false;
    }


    private double calculateTotalAmount(RentedBook book, LocalDateTime now) {
        double amount = 0;
        // Tính tổng số giờ đã mượn
        LocalDateTime rentedFrom = book.getRentedTime();
        Duration totalTime = Duration.between(now, rentedFrom);
        long hours = totalTime.toHours();
        amount *= (double) hours / 24;

        for (RentedBookPenalty p : book.getPenalties()) {
            amount += p.getFee();
        }
        return amount;
    }

}
