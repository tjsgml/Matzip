package com.itwill.matzip.controller;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.entity.RestaurantToCreateEntity;
import com.itwill.matzip.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/")
    public String getMatzipToControl() {
        return "/admin/index";
    }

    @GetMapping("/matzip/restaurant")
    public String showPageToAddMatzip() {
        return "admin/create-matzip";
    }

    @ResponseBody
    @PostMapping("/matzip/restaurant")
    public ResponseEntity<Long> addMatzip(@RequestBody RestaurantToCreateEntity restaurant) {

        Restaurant restaurantCreated = adminService.addMatzip(restaurant);

        return ResponseEntity.ok(restaurantCreated.getId());
    }
}
