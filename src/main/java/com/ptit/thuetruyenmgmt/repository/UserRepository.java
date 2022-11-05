package com.ptit.thuetruyenmgmt.repository;

import com.ptit.thuetruyenmgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Integer> {

    //    @Query(value = "CALL FIND_USER_BY_CODE(:code);", nativeQuery = true)
    T findByCode(String code);

}
