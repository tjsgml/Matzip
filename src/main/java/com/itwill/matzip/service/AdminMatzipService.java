package com.itwill.matzip.service;

import com.itwill.matzip.domain.*;
import com.itwill.matzip.domain.enums.BusinessDay;
import com.itwill.matzip.domain.enums.Expose;
import com.itwill.matzip.dto.RestaurantUpdateDto;
import com.itwill.matzip.dto.admin.*;
import com.itwill.matzip.repository.BusinessHourRepository;
import com.itwill.matzip.repository.CategoryRepository;
import com.itwill.matzip.repository.HashtagCategoryRepository;
import com.itwill.matzip.repository.MenuRepository;
import com.itwill.matzip.repository.UpdateRequest.UpdateRequestRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import com.itwill.matzip.repository.reviewHashtag.ReviewHashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMatzipService {

    private final RestaurantRepository restaurantDao;
    private final CategoryRepository categoryDao;
    private final MenuRepository menuDao;
    private final BusinessHourRepository businessHourDao;
    private final HashtagCategoryRepository hashtagCategoryDao;
    private final ReviewHashtagRepository reviewHashtagDao;
    private final UpdateRequestRepository updateRequestDao;

    public List<Category> getCategories() {
        return categoryDao.findAll();
    }

    public List<CategoryListDto> getCategoryListItems() {
        List<Category> categories = categoryDao.findByOrderByListOrderAsc();
        List<CategoryListDto> categoryListItems = new ArrayList<>();

        categories.forEach(el -> {
            CategoryListDto item = CategoryListDto.fromCategory(el);
            categoryListItems.add(item);
        });

        return categoryListItems;
    }

    public Restaurant addMatzip(RestaurantToCreateDto restaurantToAdd) {
        Restaurant restaurant = restaurantToAdd.toEntity();
        restaurantDao.save(restaurant);
        Map<String, BusinessTimeDto> businessTimes = restaurantToAdd.getBusinessTimes();

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
        bHours.forEach(el -> {
            if (el.getDays() != null) {
                businessHours.put(el.getDays().name(), el);
            }
        });
        result.put("businessHours", businessHours);

        List<BusinessDay> dayList = Arrays.stream(BusinessDay.values()).toList();
        result.put("dayList", dayList);

        return result;
    }

    public void addMenusToRestaurant(MenusToCreateDto menus) {
        Restaurant restaurant = restaurantDao.findById(menus.getRestaurantId()).orElse(null);
        menus.getMenus().forEach(el -> {
            menuDao.save(el.toEntity(restaurant));
        });
    }

    public Menu addMenuToRestaurant(Long restaurantId, MenuToCreateDto menu) {
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow();
        Menu menuToCreate = menu.toEntity(restaurant);
        menuDao.save(menuToCreate);
        return menuToCreate;
    }


    public void deleteMenuFromRestaurant(List<Long> menus) {
        menuDao.deleteAllById(menus);
    }

    public void deleteRestaurantById(Long restaurantId) {
        restaurantDao.deleteById(restaurantId);
    }

    @Transactional
    public void setStatusRestaurantById(Long restaurantId, String status) {
        log.info("상태 변화");
        Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow(null);

        if (restaurant == null) {
            return;
        }

        restaurant.updateStatus(RestaurantStatus.valueOf(status.toUpperCase()));
    }

    public Map<String, Object> getRestaurantByOptions(RestaurantSearchCond cond) {

        if (cond == null) return null;

        log.info("getRestaurantByOptions(RestaurantSearchCond={})", cond);
        Map<String, Object> result = new HashMap<>();
        Page<Restaurant> restaurants = restaurantDao.searchByPagination(cond);
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

        List<RestaurantStatus> restaurantStatuses = List.of(RestaurantStatus.values());
        result.put("restaurantStatuses", restaurantStatuses);
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
        restaurant.updateStatus(restaurantUpdateDto.getStatus());

        return restaurant;
    }

    @Transactional
    public Menu updateMenuName(Long menuId, String name) {
        Menu menu = menuDao.findById(menuId).orElseThrow();
        menu.updateName(name);
        return menu;
    }

    @Transactional
    public Menu updateMenuPrice(Long menuId, Long price) {
        Menu menu = menuDao.findById(menuId).orElseThrow();
        menu.updatePrice(price);
        return menu;
    }

    @Transactional
    public void updateRestaurantBusinessTime(List<BusinessHourUpdateDto> businessHours, Long restaurantId) {
        businessHours.forEach(el -> {
            log.info("el = {}", el);
            businessTime(el, restaurantId);
        });
    }


    private void businessTime(BusinessHourUpdateDto el, Long restaurantId) {
        if (el == null) return;

        if (el.getBhourId() == null) {
//            등록 flow
            Restaurant restaurant = restaurantDao.findById(restaurantId).orElseThrow();
            BusinessHour bHour = el.toBusinessHour(restaurant);

            businessHourDao.save(bHour);
            return;
        }

        BusinessHour bhour = businessHourDao.findById(el.getBhourId()).orElseThrow();

        if (el.getIsHoliday()) {
            bhour.updateIsHoliday(true);
            bhour.updateOpenTime(null);
            bhour.updateCloseTime(null);
            return;
        }

        bhour.updateIsHoliday(false);
        bhour.updateOpenTime(el.getStartTime());
        bhour.updateCloseTime(el.getEndTime());
//        수정 flow
    }

    @Transactional
    public void updateCategoryOrder(List<CategoryOrderUpdateDto> categoryOrderUpdateList) {
        log.info("CategoryOrderUpdateDtos = {}", categoryOrderUpdateList);
        for (CategoryOrderUpdateDto categoryToUpdate : categoryOrderUpdateList) {
            Category category = categoryDao.findById(categoryToUpdate.getId()).orElseThrow();
            category.changeOrderToShow(categoryToUpdate.getOrder());
        }
    }

    private final static Integer DEFAULT_CATEGORY = 1;

    //    TODO : 수정 필요 => restaurant 삭제되지 않고 기본 값으로 들어갈 수 있도록 해야 함.
    @Transactional
    public void deleteCategory(Integer categoryId) {
        log.info("deleteCategory(Integer categoryId = {} )", categoryId);
        categoryDao.deleteById(categoryId);
        restaurantDao.updateCategoryToDefaultCategory(categoryId, DEFAULT_CATEGORY);
    }

    public Category createCategory(String categoryName) {
        log.info("createCategory (String categoryName = {} )", categoryName);
        Category categoryFinal = categoryDao.findTop1ByOrderByListOrderDesc();
        Category category = Category.builder()
                .name(categoryName)
                .listOrder(categoryFinal == null ? 1 : categoryFinal.getListOrder() + 1)
                .build();

        categoryDao.save(category);

        return category;
    }

    @Transactional
    public void updateCategoryName(Integer categoryId, String categoryName) {
        Category category = categoryDao.findById(categoryId).orElseThrow();

        category.changeName(categoryName);
    }

    public List<HashtagCategory> getHashtagCategories() {
        return hashtagCategoryDao.findAll();
    }

    public List<ReviewHashtag> getReviewHashtags(HashtagSearchDto searchDto) {
        return reviewHashtagDao.searchReviewHashtagByKeyword(searchDto);
    }

    @Transactional
    public void updateTagExpose(Expose expose, Long... tagIdList) {
        log.info("tagIdList={}", tagIdList);
        List<Long> tagIds = List.of(tagIdList);

        for (Long tagId : tagIds) {
            ReviewHashtag hashtag = reviewHashtagDao.findById(tagId).orElseThrow();
            log.info("hash={}", hashtag);
            hashtag.changeExpose(expose);
        }
    }

    public ReviewHashtag getReviewHashtagById(Long hashtagId) {
        return reviewHashtagDao.findById(hashtagId).orElseThrow();
    }

    @Transactional
    public void updateHashtag(Long tagId, HashtagUpdateDto updateDto) {
        ReviewHashtag reviewHashtag = reviewHashtagDao.findById(tagId).orElseThrow();

        reviewHashtag.updateKeyword(updateDto.getTagName());

        log.info("tgId={}", tagId);
        log.info("updateDto={}", updateDto);
        HashtagCategory htCategory = hashtagCategoryDao.findById(updateDto.getCategoryId()).orElseThrow();
        reviewHashtag.changeCategory(htCategory);

        reviewHashtag.changeExpose(updateDto.getExpose());
    }

    public void deleteReviewHashtagById(Long... tagId) {
        reviewHashtagDao.deleteAllById(List.of(tagId));
    }

    public Page<UpdateRequest> getRequests(SearchRequestDto searchRequestDto) {
        return updateRequestDao.searchRequest(searchRequestDto);
    }

    public UpdateRequest getRequestById(Long reqId) {
        return updateRequestDao.findById(reqId).orElseThrow();
    }

    @Transactional
    public void updateRequest(Long... reqId) {
        List<UpdateRequest> updateRequest = updateRequestDao.findAllById(List.of(reqId));
        updateRequest.forEach(UpdateRequest::completeRequest);
    }
}
