package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Bill;

public interface BillService {

    Bill createBillInfo(Bill bill);

    boolean saveBillInfo(Bill bill);

}
