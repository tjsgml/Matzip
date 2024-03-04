package com.itwill.matzip.service;

import com.itwill.matzip.domain.*;
import com.itwill.matzip.dto.admin.*;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final RestaurantRepository restaurantDao;

    public List<Restaurant> getAllRestaurant(RestaurantSearchCond cond, HttpServletResponse resp) {
        final String fileName = "등록된 레스토랑 리스트";
        List<Restaurant> restaurants = restaurantDao.search(cond);

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(fileName);

            final String[] headers = {"ID", "카테고리", "가게명", "주소", "상세 주소", "전화번호", "가게상태"};

            Row row = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < restaurants.size(); i++) {
                row = sheet.createRow(i + 1);

                Restaurant restaurant = restaurants.get(i);

                Cell cell = row.createCell(0);
                cell.setCellValue(restaurant.getId());
                cell = row.createCell(1);
                cell.setCellValue(restaurant.getCategory().getName());
                cell = row.createCell(2);
                cell.setCellValue(restaurant.getName());
                cell = row.createCell(3);
                cell.setCellValue(restaurant.getAddress());
                cell = row.createCell(4);
                cell.setCellValue(restaurant.getDetailAddress());
                cell = row.createCell(5);
                cell.setCellValue(restaurant.getContact());
                cell = row.createCell(6);
                cell.setCellValue(restaurant.getStatus().getKor());
            }


            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            workbook.write(resp.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    ;
}
