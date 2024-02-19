package com.itwill.matzip.web.admin;

import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Menu;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.*;
import com.itwill.matzip.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/matzip")
public class AdminMatzipController {

    @Autowired
    AdminService adminService;

    //    레스토랑 추가 페이지로 이동
    @GetMapping("/restaurant")
    public String showPageToAddMatzip(Model model) {
        List<Category> categories = adminService.getCategories();
        model.addAttribute("categories", categories);
        return "admin/create-restaurant";
    }

    //    레스토랑 메뉴 추가 페이지로 이동
    @GetMapping("/restaurant/{restaurantId}/menu")
    public String showPageToAddMenu(@PathVariable Long restaurantId, Model model) {
        log.info("restaurantId = {}", restaurantId);
        Restaurant restaurant = adminService.getRestaurant(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "admin/update-menu";
    }

    //    레스토랑 관리 리스트
    @GetMapping("/restaurant/all")
    public String showRestaurantListPage(@ModelAttribute RestaurantSearchCond cond, Model model) {
        log.info("showRestaurantListPage(RestaurantSearchCond cond={})", cond.getKeywordCriteria());

        Map<String, Object> result = adminService.getRestaurantByOptions(cond);
        model.addAllAttributes(result);

        log.info("restaurants={}", result.get("restaurants"));
        return "admin/restaurant-list";
    }

    //    현재 사용 X : 조건 검사하여 검색하는 rest API
    @ResponseBody
    @GetMapping("/restaurant/search")
    public ResponseEntity<Map<String, Object>> searchRestaurantListPage() {
        Map<String, Object> result = adminService.getRestaurantByOptions(null);
        return ResponseEntity.ok(result);
    }

    //    레스토랑 추가
    @ResponseBody
    @PostMapping("/restaurant")
    public ResponseEntity<Long> addMatzip(@RequestBody RestaurantToCreateDto restaurant) {
        log.info("addMatzip(restaurant : {})", restaurant);

        Restaurant restaurantCreated = adminService.addMatzip(restaurant);

        log.info("restaurantCreated={}", restaurantCreated);
        return ResponseEntity.ok(restaurantCreated.getId());
    }

    //    레스토랑 삭제 (폐업 처리된 레스토랑 삭제)
    @ResponseBody
    @DeleteMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Void> deleteMatzipData(@PathVariable Long restaurantId) {
        adminService.deleteRestaurantById(restaurantId);
        return ResponseEntity.noContent().build();
    }

    //    메뉴 등록하는 REST API
    @ResponseBody
    @PostMapping("/restaurant/{restaurantId}/menu")
    public ResponseEntity<URI> addMenuToRestaurant(@RequestBody MenusToCreateDto menus, @PathVariable Long restaurantId) {
        menus.setRestaurantId(restaurantId);

        adminService.addMenusToRestaurant(menus);
        URI url = URI.create("./");
        return ResponseEntity.created(url).build();
    }

    @ResponseBody
    @PostMapping("/restaurant/{restaurantId}/menu/one")
    public ResponseEntity<Long> addMenuToRestaurant(@RequestBody MenuToCreateDto menu, @PathVariable Long restaurantId) {
        log.info("addMenuToRestaurant(MenuToCreateDto={})",menu);
        Menu menuCreated = adminService.addMenuToRestaurant(restaurantId, menu);

        return ResponseEntity.ok(menuCreated.getId());
    }

    //    메뉴 삭제 REST API
    @ResponseBody
    @DeleteMapping("/restaurant/menu")
    public ResponseEntity<Void> deleteMenuToRestaurant(@RequestParam List<Long> menuId) {

        log.info("deleteMenuToRestaurant(menus={})", menuId);

        adminService.deleteMenuFromRestaurant(menuId);

        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PatchMapping("/restaurant/menu/{menuId}/price")
    public ResponseEntity<Menu> updateMenuPriceToRestaurant(@PathVariable Long menuId, @RequestParam Long price) {
        Menu menu = adminService.updateMenuPrice(menuId, price);
        return ResponseEntity.ok(menu);
    }

    @ResponseBody
    @PatchMapping("/restaurant/menu/{menuId}/name")
    public ResponseEntity<Menu> updateMenuNameToRestaurant(@PathVariable Long menuId, @RequestParam String name) {
        Menu menu = adminService.updateMenuName(menuId, name);
        return ResponseEntity.ok(menu);
    }

    @ResponseBody
    @PutMapping("/restaurant/{restaurantId}/{status}")
    public ResponseEntity<Void> setRestaurantById(
            @PathVariable Long restaurantId,
            @PathVariable String status
    ) {
        adminService.setStatusRestaurantById(restaurantId, status);
        return ResponseEntity.noContent().build();
    }

    //    레스토랑 디테일 페이지로 이동
    @GetMapping("/restaurant/{restaurantId}")
    public String getRestaurantDetail(@PathVariable Long restaurantId, Model model) {
        Map<String, Object> result = adminService.getRestaurantForDetail(restaurantId);
        model.addAllAttributes(result);
        return "admin/detail-restaurant";
    }

    @GetMapping("/restaurant/{restaurantId}/info")
    public String getRestaurantUpdatePage(@PathVariable Long restaurantId, Model model) {

        Map<String, Object> result = adminService.getRestaurantInfoForUpdate(restaurantId);
        model.addAllAttributes(result);
        return "admin/update-restaurant";
    }

    @PatchMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurantInfo(@RequestBody RestaurantUpdateDto restaurantUpdateDto) {
        Restaurant updatedRestaurant = adminService.updateRestaurant(restaurantUpdateDto);
        return ResponseEntity.ok(updatedRestaurant);
    }


}
