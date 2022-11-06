package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.RentedBookPenalty;
import com.ptit.thuetruyenmgmt.model.key.RentedBookPenaltyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentedBookPenaltyRepository extends JpaRepository<RentedBookPenalty, RentedBookPenaltyKey> {

    List<RentedBookPenalty> findAllByRentedBook_Id(Integer rentedBookId);

}
