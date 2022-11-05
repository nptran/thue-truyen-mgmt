package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {
}
