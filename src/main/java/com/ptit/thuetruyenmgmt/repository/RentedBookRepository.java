package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentedBookRepository extends JpaRepository<RentedBook, Integer> {

//    List<RentedBook> findAllByCustomer_IdAndIsPaidIsFalse(int customerId);

//    List<RentedBook> findAllByIdAndIsPaidIsFalse(List<Integer> ids);

}
