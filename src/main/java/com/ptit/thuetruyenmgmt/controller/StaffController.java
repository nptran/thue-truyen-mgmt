package com.ptit.thuetruyenmgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaffController {

    @RequestMapping(path= {"/", "home"})
    public String home() {
        return "home";
    }

}
