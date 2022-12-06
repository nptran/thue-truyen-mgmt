package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.exception.FailedToPayException;
import com.ptit.thuetruyenmgmt.exception.NoneSelectedBookToReturnException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.*;
import com.ptit.thuetruyenmgmt.model.dto.RentedBookDTO;
import com.ptit.thuetruyenmgmt.repository.BillRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.repository.StaffRepository;
import com.ptit.thuetruyenmgmt.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Autowired
    private RentedBookRepository rentedBookRepository;

    @Autowired
    private StaffRepository staffRepository;


    private static final String STAFF_RESOURCE = Staff.class.getSimpleName();


    @Override
    @Transactional
    public Bill createPayInfo(List<RentedBook> rentedBooks, Integer staffId) {
        if (rentedBooks == null || rentedBooks.isEmpty()) {
            throw new NoneSelectedBookToReturnException();
        }

        Optional<Staff> staffOptional = staffRepository.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new NotFoundException(STAFF_RESOURCE);
        }
        Staff staff = staffOptional.get();

        LocalDateTime now = LocalDateTime.now();
        double totalAmount = 0;
        for (RentedBook book : rentedBooks) {
            totalAmount += calculateTotalAmount(book);
        }

        return Bill.builder()
                .createTime(now)
                .totalAmount(Math.round(totalAmount))
                .rentedBooks(rentedBooks)
                .staff(staff)
                .build();
    }

    @Override
    @Transactional
    public boolean saveBillInfo(Bill bill) {
        if (bill.getRentedBooks() == null || bill.getRentedBooks().isEmpty()) {
            throw new NoneSelectedBookToReturnException();
        }
        try {
            repository.save(bill);
        } catch (Exception e) {
            throw new FailedToPayException();
        }
        for (RentedBook paidBook: bill.getRentedBooks()) {
            paidBook.setPaid(true);
        }
        return updateRentStatus(bill.getRentedBooks());
    }


    private boolean updateRentStatus(List<RentedBook> paidBooks) {
        // Kiểm tra sự tồn tại trong CSDL
        for (RentedBook paidBook : paidBooks) {
            int id = paidBook.getId();
            Optional<RentedBook> optional = rentedBookRepository.findById(id);
            if (!optional.isPresent()) {
                throw new FailedToPayException("Đầu truyện ID `" + id + "` không tồn tại.");
            }
        }
        try {
            rentedBookRepository.saveAllAndFlush(paidBooks);
        } catch (Exception e) {
            throw new FailedToPayException(e.getMessage());
        }
        return true;
    }


    private double calculateTotalAmount(RentedBook book) {
        // Tính tiền thuê chưa gồm phí phạt
        double amount = RentedBookDTO.calculateAmountTilToday(book);

        for (RentedBookPenalty p : book.getPenalties()) {
            amount += p.getFee();
        }
        return amount;
    }

}
