package com.itwill.matzip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.HashtagCategory;

public interface HashtagCategoryRepository extends JpaRepository<HashtagCategory, Long> {
	Optional<HashtagCategory> findByName(String name);
}
