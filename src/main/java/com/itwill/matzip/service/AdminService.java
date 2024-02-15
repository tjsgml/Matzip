package com.itwill.matzip.service;

import com.itwill.matzip.domain.BusinessHour;
import com.itwill.matzip.domain.Category;
import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.domain.RestaurantStatus;
import com.itwill.matzip.domain.enums.BusinessDay;
import com.itwill.matzip.dto.*;
import com.itwill.matzip.repository.BusinessHourRepository;
import com.itwill.matzip.repository.CategoryRepository;
import com.itwill.matzip.repository.MenuRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.*;

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

    public Map<String, Object> getRestaurantForDetail(Long restaurantId) {
        Map<String, Object> result = new HashMap<>();
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElse(null);
        result.put("restaurant", restaurant);

        List<BusinessHour> bHours = businessHourDao.findByRestaurant(restaurant);
        Map<String, BusinessHour> businessHours = new HashMap<>();
        bHours.forEach(el -> businessHours.put(el.getDays().getKor().trim(), el));
        businessHours.keySet().forEach(e -> System.out.println(e));
        result.put("businessHours", businessHours);

        List<BusinessDay> dayValue = Arrays.stream(BusinessDay.values()).toList();
        List<String> days = new ArrayList<>();
        dayValue.forEach(el -> days.add(el.getKor().trim()));
//        days.forEach(System.out::println);
        result.put("days", days);
        log.info("businessHoursday222 = {}", businessHours.get(days.get(2)));

        return result;
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

        if (cond == null) return null;

        log.info("getRestaurantByOptions(RestaurantSearchCond={})", cond);
        Map<String, Object> result = new HashMap<>();
        Page<Restaurant> restaurants = restaurantDao.search(cond);
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

    public Map<String, Object> getRestaurantInfoForUpdate(Long restaurantId) {
        log.info("getRestaurantInfoForUpdate(restaurantId={})", restaurantId);
        Map<String, Object> result = new HashMap<>();
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElse(null);
        result.put("restaurant", restaurant);

        List<Category> categories = getCategories();
        result.put("categories", categories);

        return result;
    }

    @Transactional
    public Restaurant updateRestaurant(RestaurantUpdateDto restaurantUpdateDto) {
        log.info("updateRestaurant(RestaurantUpdateDto={})", restaurantUpdateDto);
        Restaurant restaurant = restaurantDao.findById(restaurantUpdateDto.getRestaurantId()).orElseThrow();

        restaurant.updateCategory(restaurantUpdateDto.getCategoryToUpdate());
        restaurant.updateName(restaurantUpdateDto.getPlaceName());
        restaurant.updateAddress(restaurantUpdateDto.getAddress());
        restaurant.updateDetailAddress(restaurantUpdateDto.getDetailAddress());
        restaurant.updateContact(restaurantUpdateDto.getContact());
        restaurant.updateLon(restaurantUpdateDto.getLon());
        restaurant.updateLat(restaurantUpdateDto.getLat());

        return restaurant;
    }
}
