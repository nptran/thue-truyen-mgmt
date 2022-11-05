package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;

import java.util.List;

public interface RentedBookService {

    List<RentedBook> getRentedBooksByCustomer(int customerId);

    RentedBook addPenaltiesIntoRentedBook(List<RentedBookPenalty> penalties, int id);

    boolean updateRentStatus(List<Integer> payBookIds);

}
