package com.itwill.matzip.repository.UpdateRequest;

import com.itwill.matzip.domain.UpdateRequest;
import com.itwill.matzip.dto.admin.SearchRequestDto;
import org.springframework.data.domain.Page;

public interface UpdateRequestQuerydsl {
    Page<UpdateRequest> searchRequest (SearchRequestDto searchRequestDto);
}
