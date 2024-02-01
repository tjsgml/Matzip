package com.itwill.matzip.service;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.entity.RestaurantToCreateEntity;
import com.itwill.matzip.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    RestaurantRepository restaurantDao;

    public Restaurant addMatzip(RestaurantToCreateEntity restaurantToAdd) {
        Restaurant restaurant = restaurantToAdd.toEntity();
        restaurantDao.save(restaurant);
        return restaurant;
    }

}
