package com.ptit.thuetruyenmgmt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(StaffController.class)
public class StaffControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Kiểm tra hiển thị gd-chinh-nv
     */
    @Test
    public void testHome_whenEmptyPath() throws Exception {
        HttpSession actualSession = this.mvc.perform(get("/"))
                .andExpect(view().name("gd-chinh-nv"))
                .andExpect(model().attribute("staffName", "Phạm Hải Yến"))
                .andReturn().getRequest().getSession();
        assert actualSession != null;
        assert actualSession.getAttribute("loginCode") != null;
        assert actualSession.getAttribute("id") != null;
    }

    /**
     * Kiểm tra hiển thị gd-chinh-nv
     */
    @Test
    public void testHome_whenHomePath() throws Exception {
        HttpSession actualSession = this.mvc.perform(get("/"))
                .andExpect(view().name("gd-chinh-nv"))
                .andExpect(model().attribute("staffName", "Phạm Hải Yến"))
                .andReturn().getRequest().getSession();
        assert actualSession != null;
        assert actualSession.getAttribute("loginCode") != null;
        assert actualSession.getAttribute("loginCode") != null;
    }

}
