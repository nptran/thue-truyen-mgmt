package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Penalty;
import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;

import java.time.LocalDateTime;
import java.util.List;

public interface RentedBookService {

    List<RentedBook> getRentedBooksByCustomer(int customerId);

    RentedBook getRentedBookById(int rentedBookId);

    List<RentedBook> getRentedBooksById(List<Integer> rentedBookIds);

    RentedBook addPenaltiesIntoRentedBook(List<RentedBookPenalty> penalties, List<RentedBookPenaltyKey> removedPenalties, int id);

}
