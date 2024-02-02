package com.itwill.matzip.controller;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.RestaurantToCreateEntity;
import com.itwill.matzip.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("addMatzip(restaurant : {})" ,restaurant);

        Restaurant restaurantCreated = adminService.addMatzip(restaurant);

        log.info("restaurantCreated={}", restaurantCreated);
        return ResponseEntity.ok(restaurantCreated.getId());
    }
}
