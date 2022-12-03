package com.ptit.thuetruyenmgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class StaffController {

    @GetMapping(path = {"/", "home"})
    public ModelAndView home(HttpSession session) {
        // Lưu lại các thông tin này sau khi NV đăng nhập thành công
        session.setAttribute("loginCode", "NV00651");
        session.setAttribute("id", 1);
        ModelAndView mav = new ModelAndView("gd-chinh-nv");
        mav.addObject("staffName", "Phạm Hải Yến");
        return mav;
    }

}
