package com.itwill.matzip.service;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.BusinessTime;
import com.itwill.matzip.dto.BusinessTimePerWeek;
import com.itwill.matzip.dto.RestaurantToCreateEntity;
import com.itwill.matzip.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {

    @Autowired
    RestaurantRepository restaurantDao;

    public Restaurant addMatzip(RestaurantToCreateEntity restaurantToAdd) {
        Restaurant restaurant = restaurantToAdd.toEntity();
        restaurantDao.save(restaurant);

        BusinessTimePerWeek businessTimes = restaurantToAdd.getBusinessTime();
        return restaurant;
    }

    public Restaurant getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElse(null);
        log.info("restaurant = {}", restaurant);
        log.info("restaurant.menus = {}", restaurant);
        return restaurant;
    }

}
