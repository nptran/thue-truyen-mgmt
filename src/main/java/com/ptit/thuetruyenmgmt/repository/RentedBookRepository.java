package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.RentedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentedBookRepository extends JpaRepository<RentedBook, Integer> {
}
