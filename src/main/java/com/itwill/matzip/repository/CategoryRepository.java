package com.itwill.matzip.repository;

import com.itwill.matzip.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    @Query("select c from Category c order by c.listOrder asc")
     List<Category> findByOrderByListOrderAsc();

     Category findTop1ByOrderByListOrderDesc();
}
