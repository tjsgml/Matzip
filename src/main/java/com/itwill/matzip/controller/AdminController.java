package com.itwill.matzip.controller;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.MenusToCreate;
import com.itwill.matzip.dto.RestaurantToCreateEntity;
import com.itwill.matzip.service.AdminService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/")
    public String getMatzipToControl() {
        return "admin/index";
    }

    @GetMapping("/matzip/restaurant")
    public String showPageToAddMatzip() {
        return "admin/create-matzip";
    }

    @GetMapping("/matzip/restaurant/{restaurantId}/menu")
    public String showPageToAddMenu(@PathVariable Long restaurantId, Model model) {
        log.info("restaurantId = {}", restaurantId);
        Restaurant restaurant = adminService.getRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "admin/update-menu";
    }

    @ResponseBody
    @PostMapping("/matzip/restaurant")
    public ResponseEntity<Long> addMatzip(@RequestBody RestaurantToCreateEntity restaurant) {
        log.info("addMatzip(restaurant : {})", restaurant);

        Restaurant restaurantCreated = adminService.addMatzip(restaurant);

        log.info("restaurantCreated={}", restaurantCreated);
        return ResponseEntity.ok(restaurantCreated.getId());
    }

    @ResponseBody
    @PostMapping("/matzip/restaurant/{restaurantId}/menu")
    public ResponseEntity<URI> addMenuToRestaurant(@RequestBody MenusToCreate menus, @PathVariable Long restaurantId) {
        menus.setRestaurantId(restaurantId);

        adminService.addMenuToRestaurant(menus);
        URI url = URI.create("./");
        return ResponseEntity.created(url).build();
    }

    @ResponseBody
    @DeleteMapping("/matzip/restaurant/menu")
    public ResponseEntity<Void> deleteMenuToRestaurant(@RequestParam List<Long> menus) {

        log.info("deleteMenuToRestaurant(menus={})", menus);

        adminService.deleteMenuFromRestaurant(menus);

        return ResponseEntity.noContent().build();
    }


}
