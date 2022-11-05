package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends UserRepository<Staff> {
}
