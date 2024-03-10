package com.itwill.matzip.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.matzip.dto.SearchListDto;
import com.itwill.matzip.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchSvc;

    @GetMapping("/all")
    public String showSearchList(@RequestParam("keyword") String keyword, @RequestParam("categoryId") int categoryId, Model model) {

        log.info("@@@@@키워드={}, 카테고리 아이디={}", keyword, categoryId);

        List<SearchListDto> list = new ArrayList<SearchListDto>();

        switch (categoryId) {
            case 1://전체
                list = searchSvc.searchAll(keyword);
                model.addAttribute("searchCategory", "전체");
                break;
            case 2://가게명
                list = searchSvc.searchByRestaurantName(keyword);
                model.addAttribute("searchCategory", "가게명");
                break;
            case 3://방문 목적
                list = searchSvc.searchByCategoryAndKeyword(1L, keyword);
                model.addAttribute("searchCategory", "방문 목적");
                break;
            case 4://분위기
                list = searchSvc.searchByCategoryAndKeyword(2L, keyword);
                model.addAttribute("searchCategory", "분위기");
                break;
            case 5://편의시설
                list = searchSvc.searchByCategoryAndKeyword(3L, keyword);
                model.addAttribute("searchCategory", "편의시설");
                break;
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("list", list);

        return "restaurant/searchList";
    }
}
