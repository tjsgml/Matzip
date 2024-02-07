package com.itwill.matzip.service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.enums.BusinessDay;
import com.itwill.matzip.dto.BusinessTime;
import com.itwill.matzip.dto.MenusToCreate;
import com.itwill.matzip.dto.RestaurantToCreateEntity;
import com.itwill.matzip.repository.BusinessHourRepository;
import com.itwill.matzip.repository.CategoryRepository;
import com.itwill.matzip.repository.MenuRepository;
import com.itwill.matzip.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Restaurant addMatzip(RestaurantToCreateEntity restaurantToAdd) {
        Restaurant restaurant = restaurantToAdd.toEntity();
        restaurantDao.save(restaurant);

        Map<String, BusinessTime> businessTimes = restaurantToAdd.getBusinessTimes();
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
        log.info("restaurant.menus = {}", restaurant);
        return restaurant;
    }

    public void addMenuToRestaurant(MenusToCreate menus) {
        Restaurant restaurant = restaurantDao.findById(menus.getRestaurantId()).orElse(null);

        menus.getMenus().forEach(el -> {
            menuDao.save(el.toEntity(restaurant));
        });
    }

    public void deleteMenuFromRestaurant(List<Long> menus) {
        menuDao.deleteAllById(menus);
    }
}
