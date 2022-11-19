package com.ptit.thuetruyenmgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class StaffController {

    @RequestMapping(path = {"/", "home"})
    public String home(HttpSession session) {
        // Lưu lại các thông tin này sau khi NV đăng nhập thành công
        session.setAttribute("loginCode", "NV00651");
        session.setAttribute("id", 1);
        return "gd-chinh-nv";
    }

}
