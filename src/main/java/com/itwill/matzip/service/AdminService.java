package com.itwill.matzip.service;

import com.itwill.matzip.domain.*;
import com.itwill.matzip.dto.admin.*;
import com.itwill.matzip.repository.member.MemberRepository;
import com.itwill.matzip.repository.restaurant.RestaurantRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MemberRepository memberDao;


    public void getAllRestaurantByExcel(RestaurantSearchCond cond, HttpServletResponse resp) {
        final String fileName = "등록된 레스토랑 리스트";
        List<Restaurant> restaurants = restaurantDao.search(cond);

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(fileName);

            final String[] headers = {"ID", "카테고리", "가게명", "주소", "상세 주소", "전화번호", "가게상태"};

            Row row = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                CellStyle headerStyle = setCellStyleHeader(workbook);
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < restaurants.size(); i++) {
                CellStyle bodyStyle = setCellStyleBody(workbook);
                row = sheet.createRow(i + 1);

                Restaurant restaurant = restaurants.get(i);

                Cell cell = row.createCell(0);
                cell.setCellValue(restaurant.getId());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(1);
                cell.setCellValue(restaurant.getCategory().getName());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(2);
                cell.setCellValue(restaurant.getName());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(3);
                cell.setCellValue(restaurant.getAddress());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(4);
                cell.setCellValue(restaurant.getDetailAddress());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(5);
                cell.setCellValue(restaurant.getContact());
                cell.setCellStyle(bodyStyle);
                cell = row.createCell(6);
                cell.setCellValue(restaurant.getStatus().getKor());
                cell.setCellStyle(bodyStyle);
            }

            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            workbook.write(resp.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getAllMemberByExcel(MemberFilterDto cond, HttpServletResponse resp) {
        final String fileName = "멤버 리스트";
        List<Member> members = memberDao.findMemberList(cond);

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(fileName);

            final String[] headers = {"ID", "아이디", "닉네임", "이메일", "역할", "회원 구분"};

            Row row = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < members.size(); i++) {
                row = sheet.createRow(i + 1);

                Member member = members.get(i);

                Cell cell = row.createCell(0);
                cell.setCellValue(member.getId());

                cell = row.createCell(1);
                cell.setCellValue(member.getUsername());

                cell = row.createCell(2);
                cell.setCellValue(member.getNickname());

                cell = row.createCell(3);
                cell.setCellValue(member.getEmail());

                List<String> roles = new ArrayList<>();
                member.getRoles().forEach(el -> roles.add(el.name()));

                cell = row.createCell(4);
                cell.setCellValue(String.join(", ", roles));

                cell = row.createCell(5);
                cell.setCellValue(member.getKakaoClientId() != null ? "카카오" : "플렛폼");
            }


            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            workbook.write(resp.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CellStyle setCellStyleHeader(Workbook workbook) {
        CellStyle TestStyle = workbook.createCellStyle();
        Font TestFont = workbook.createFont();
        TestStyle.setBorderLeft(BorderStyle.THICK);

        TestStyle.setBorderRight(BorderStyle.THICK);
        TestStyle.setBorderBottom(BorderStyle.THICK);
        TestStyle.setBorderTop(BorderStyle.THICK);

        TestFont.setBold(true);
        TestFont.setFontHeight((short) 260);
        TestFont.setFontName("맑은 고딕");

        TestStyle.setFont(TestFont);

        return TestStyle;
    }

    private CellStyle setCellStyleBody(Workbook workbook) {
        CellStyle TestStyle = workbook.createCellStyle();
        Font TestFont = workbook.createFont();
        TestStyle.setBorderLeft(BorderStyle.THIN);

        TestStyle.setBorderRight(BorderStyle.THIN);
        TestStyle.setBorderBottom(BorderStyle.THIN);
        TestStyle.setBorderTop(BorderStyle.THIN);

        TestFont.setBold(false);
        TestFont.setFontHeight((short) 200);
        TestFont.setFontName("맑은 고딕");

        TestStyle.setFont(TestFont);

        return TestStyle;
    }


}
