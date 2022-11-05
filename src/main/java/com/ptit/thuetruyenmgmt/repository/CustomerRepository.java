package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "CALL SEARCH_CUSTOMER_BY_NAME(:kw);", nativeQuery = true)
    List<Customer> findByName(@Param("kw") String name);

}
