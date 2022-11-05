package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentedBookPenaltyRepository extends JpaRepository<RentedBookPenalty, Integer> {
}
