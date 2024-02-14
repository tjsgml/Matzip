package com.itwill.matzip.web;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.MenusToCreateDto;
import com.itwill.matzip.dto.RestaurantSearchCond;
import com.itwill.matzip.dto.RestaurantToCreateDto;
import com.itwill.matzip.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public String showPageToAddMatzip(Model model) {
        List<Category> categories = adminService.getCategories();
        model.addAttribute("categories", categories);
        return "admin/create-matzip";
    }

    @GetMapping("/matzip/restaurant/{restaurantId}/menu")
    public String showPageToAddMenu(@PathVariable Long restaurantId, Model model) {
        log.info("restaurantId = {}", restaurantId);
        Restaurant restaurant = adminService.getRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "admin/update-menu";
    }

    @GetMapping("/matzip/restaurant/all")
    public String showRestaurantListPage(@ModelAttribute RestaurantSearchCond cond, Model model) {
        log.info("showRestaurantListPage(RestaurantSearchCond cond={})", cond);

        Map<String, Object> result = adminService.getRestaurantByOptions(cond);
        model.addAllAttributes(result);

        log.info("restaurants={}", result.get("restaurants"));
        return "admin/restaurant-list";
    }

    @ResponseBody
    @GetMapping("/matzip/restaurant/search")
    public ResponseEntity<Map<String, Object>> searchRestaurantListPage() {
        Map<String, Object> result = adminService.getRestaurantByOptions(null);
        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @DeleteMapping("/matzip/restaurant")
    public ResponseEntity<Long> addMatzip(@RequestBody RestaurantToCreateDto restaurant) {
        log.info("addMatzip(restaurant : {})", restaurant);

        Restaurant restaurantCreated = adminService.addMatzip(restaurant);

        log.info("restaurantCreated={}", restaurantCreated);
        return ResponseEntity.ok(restaurantCreated.getId());
    }

//    @ResponseBody
//    @PostMapping("/matzip/restaurant/{restaurantId}")
//    public ResponseEntity<String> addMatzip(@PathVariable Long restaurantId) {
//        adminService.deleteRestaurantById(restaurantId);
//        return ResponseEntity.noContent());
//    }

    @ResponseBody
    @PostMapping("/matzip/restaurant/{restaurantId}/menu")
    public ResponseEntity<URI> addMenuToRestaurant(@RequestBody MenusToCreateDto menus, @PathVariable Long restaurantId) {
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

    @ResponseBody
    @PutMapping("/matzip/restaurant/{restaurantId}/{status}")
    public ResponseEntity<Void> setRestaurantById(
            @PathVariable Long restaurantId,
            @PathVariable String status
    ) {
        adminService.setStatusRestaurantById(restaurantId, status);
        return ResponseEntity.noContent().build();
    }


}
