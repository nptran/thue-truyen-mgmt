package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class RentedBookServiceImpl implements RentedBookService {

    @Autowired
    private RentedBookRepository repository;

    @Autowired
    private RentedBookPenaltyRepository rentedBookPenaltyRepository;

    private static final String RESOURCE_NAME = Customer.class.getSimpleName();


    @Override
    public List<RentedBook> getRentedBooksByCustomer(int customerId) {
        return repository.findAllByCustomer_IdAndIsPaidIsFalse(customerId);
    }

    @Override
    public RentedBook getRentedBookById(int rentedBookId) {
        Optional<RentedBook> optional = repository.findById(rentedBookId);
        if (!optional.isPresent()) {
            throw new NotFoundException(RESOURCE_NAME);
        }
        RentedBook rentedBook = optional.get();
        List<RentedBookPenalty> rentedBookPenalties = rentedBookPenaltyRepository.findAllByRentedBook_Id(rentedBookId);
        rentedBook.setPenalties(rentedBookPenalties);
        return optional.get();
    }

    @Override
    public RentedBook addPenaltiesIntoRentedBook(List<RentedBookPenalty> penalties, int id) {
        rentedBookPenaltyRepository.saveAll(penalties);
        repository.findById(id);
        Optional<RentedBook> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(RESOURCE_NAME);
        }
        return optional.get();
    }

    @Override
    public boolean updateRentStatus(List<Integer> payBookIds) {
        return false;
    }

    @Override
    public List<Penalty> rentedBookPenaltiesToPenalties(List<RentedBookPenalty> rentedBookPenalties) {
        List<Penalty> penalties = new ArrayList<>();
        if (rentedBookPenalties != null) {
            for (RentedBookPenalty r : rentedBookPenalties) {
                Penalty p = r.getPenalty();
                p.setRecommendedFee(r.getFee());
                penalties.add(p);
            }
        }
        return penalties;
    }
}
