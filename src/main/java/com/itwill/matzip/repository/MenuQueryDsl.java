package com.itwill.matzip.repository;

import org.springframework.data.querydsl.binding.QuerydslPredicate;

import java.util.List;

@QuerydslPredicate
public interface MenuQueryDsl {

    void deleteMenus(List<Long> menus);

}
