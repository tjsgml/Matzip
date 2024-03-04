package com.itwill.matzip.repository.UpdateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.matzip.domain.UpdateRequest;

public interface UpdateRequestRepository extends JpaRepository<UpdateRequest, Long>, UpdateRequestQuerydsl{
}
