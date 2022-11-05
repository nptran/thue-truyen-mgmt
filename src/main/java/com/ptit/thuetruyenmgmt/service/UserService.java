package com.ptit.thuetruyenmgmt.service;


import com.ptit.thuetruyenmgmt.model.User;

public interface UserService<T extends User> {

    T login(String code);

}
