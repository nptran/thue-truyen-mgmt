package com.ptit.thuetruyenmgmt.service.impl;

import com.ptit.thuetruyenmgmt.exception.FailedToPayException;
import com.ptit.thuetruyenmgmt.exception.FailedToResetBookPenaltiesException;
import com.ptit.thuetruyenmgmt.exception.NotFoundException;
import com.ptit.thuetruyenmgmt.model.Customer;
import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import com.ptit.thuetruyenmgmt.repository.RentedBookPenaltyRepository;
import com.ptit.thuetruyenmgmt.repository.RentedBookRepository;
import com.ptit.thuetruyenmgmt.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<RentedBook> getRentedBooksById(List<Integer> rentedBookIds) {
        List<RentedBook> rbs = new ArrayList<>();
        for (int id : rentedBookIds) {
            Optional<RentedBook> optional = repository.findById(id);
            optional.ifPresent(rbs::add);
        }
        return rbs;
    }


    @Override
    public RentedBook addPenaltiesIntoRentedBook(List<RentedBookPenalty> penalties, List<RentedBookPenaltyKey> removedIds, int id) {
        try {
            // Xoá các RentedBookPenalty hiện tại
            if (removedIds != null && !removedIds.isEmpty()) {
                rentedBookPenaltyRepository.deleteAllByIdInBatch(removedIds);
                rentedBookPenaltyRepository.flush();
            }

            // Lưu lại DS các RentedBookPenalty mới
            if (penalties != null && !penalties.isEmpty()) {
                rentedBookPenaltyRepository.saveAllAndFlush(penalties);
            }
        } catch (Exception e) {
            throw new FailedToResetBookPenaltiesException(e.getMessage());
        }
        Optional<RentedBook> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException(RESOURCE_NAME);
        }
        return optional.get();
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
