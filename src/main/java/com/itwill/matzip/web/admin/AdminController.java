package com.itwill.matzip.web.admin;

import com.itwill.matzip.domain.Restaurant;
import com.itwill.matzip.dto.admin.RestaurantSearchCond;
import com.itwill.matzip.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("")
    public String getMatzipToControl() {
        return "admin/index";
    }

    @ResponseBody
    @GetMapping("/excel/restaurant")
    public void downloadExcel(RestaurantSearchCond cond, HttpServletResponse response) {
        adminService.getAllRestaurant(cond, response);
    }

}
