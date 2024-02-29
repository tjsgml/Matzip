package com.itwill.matzip.dto.admin;

import com.itwill.matzip.dto.admin.MenuToCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenusToCreateDto {
    Long restaurantId;
    List<MenuToCreateDto> menus;

    public void setMenus(MenuToCreateDto... menus) {
        this.menus = Arrays.stream(menus).toList();
    }
}
