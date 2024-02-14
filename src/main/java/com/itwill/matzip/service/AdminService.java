package com.itwill.matzip.service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.dto.BusinessTimeDto;
import com.itwill.matzip.dto.MenusToCreateDto;
import com.itwill.matzip.dto.RestaurantSearchCond;
import com.itwill.matzip.dto.RestaurantToCreateDto;
import com.itwill.matzip.repository.BusinessHourRepository;
import com.itwill.matzip.repository.CategoryRepository;
import com.itwill.matzip.repository.MenuRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminService {

    @Autowired
    RestaurantRepository restaurantDao;

    @Autowired
    CategoryRepository categoryDao;

    @Autowired
    MenuRepository menuDao;

    @Autowired
    BusinessHourRepository businessHourDao;

    public List<Category> getCategories() {
        return categoryDao.findAll();
    }

    public Restaurant addMatzip(RestaurantToCreateDto restaurantToAdd) {
        Restaurant restaurant = restaurantToAdd.toEntity();
        restaurantDao.save(restaurant);
        Map<String, BusinessTimeDto> businessTimes = restaurantToAdd.getBusinessTimes();
        log.info("businessTimes11={}", businessTimes);

        for (String day : businessTimes.keySet()) {
            log.info("day={}", day);

            if (businessTimes.get(day) == null) {
                continue;
            }

            BusinessHour businessHour = businessTimes.get(day).toEntity(restaurant);
            businessHourDao.save(businessHour);
        }

        return restaurant;
    }

    public Restaurant getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElse(null);
        log.info("restaurant = {}", restaurant);
        log.info("restaurant.menus = {}", restaurant.getMenus());
        return restaurant;
    }

    public void addMenuToRestaurant(MenusToCreateDto menus) {
        Restaurant restaurant = restaurantDao.findById(menus.getRestaurantId()).orElse(null);
        menus.getMenus().forEach(el -> {
            menuDao.save(el.toEntity(restaurant));
        });
    }

    public void deleteMenuFromRestaurant(List<Long> menus) {
        menuDao.deleteAllById(menus);
    }

    public void deleteRestaurantById(Long restaurantId) {
        restaurantDao.deleteById(restaurantId);
    }

    public void setStatusRestaurantById(Long restaurantId, String status) {
        log.info("상태 변화");
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElse(null);

        if (restaurant == null) {
            return;
        }

        Restaurant restaurantToUpdate = Restaurant.builder()
                .id(restaurantId)
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .detailAddress(restaurant.getDetailAddress())
                .contact(restaurant.getContact())
                .lon(restaurant.getLon())
                .lat(restaurant.getLat())
                .status(RestaurantStatus.valueOf(status.toUpperCase()))
                .category(restaurant.getCategory())
                .build();

        log.info("restaurantToUpdate={}", restaurantToUpdate);
        restaurantDao.save(restaurantToUpdate);
    }

    public Map<String, Object> getRestaurantByOptions(RestaurantSearchCond cond) {
        Map<String, Object> result = new HashMap<>();
        log.info("getRestaurantByOptionsser(RestaurantSearchCond={})", cond);
        Page<Restaurant> restaurants = restaurantDao.search(cond);
        log.info("Page<Restaurant> restaurants = restaurantDao.search(cond);= {}", restaurants.getContent());
        result.put("restaurants", restaurants);

        List<Category> categories = getCategories();
        result.put("categories", categories);

        result.put("category", cond.getCategoryCond() != null ? cond.getCategoryCond() : null);
        result.put("status", cond.getRestaurantStatus());
        result.put("order", cond.getOrder());

        result.put("CLOSURE", RestaurantStatus.CLOSURE);
        result.put("OPEN", RestaurantStatus.OPEN);
        result.put("WAIT", RestaurantStatus.WAIT);
        log.info("categories={}", categories);

        return result;
    }
}
