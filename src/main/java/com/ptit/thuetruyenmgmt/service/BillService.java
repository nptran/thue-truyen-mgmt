package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Bill;
import com.ptit.thuetruyenmgmt.model.RentedBook;

import java.util.List;

public interface BillService {

    Bill createPayInfo(List<RentedBook> rentedBooks, Integer staffId);

    boolean saveBillInfo(Bill bill);

}
