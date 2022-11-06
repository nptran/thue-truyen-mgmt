package com.ptit.thuetruyenmgmt.service;

import com.ptit.thuetruyenmgmt.model.Penalty;

import java.util.List;

public interface PenaltyService {
    List<Penalty> getAllPenalties();

    Penalty getPenaltyById(int id);

}
