package com.itwill.matzip.web.admin;

import com.itwill.matzip.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("")
    public String getMatzipToControl() {
        return "admin/index";
    }

}
