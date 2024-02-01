package com.itwill.matzip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminMapController {

    FileCopyUtils fileCopyUtils;

    @GetMapping("/")
    public String getMatzipToControl () {
        return "/admin/index";
    }

    @GetMapping("/matzip/restaurant")
    public String addMatzip () {
        return "admin/create-matzip";
    }
}
