package com.itwill.matzip.repository.UpdateRequest;


import com.itwill.matzip.domain.QUpdateRequest;
import com.itwill.matzip.domain.UpdateRequest;
import com.itwill.matzip.dto.admin.SearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
public class UpdateRequestQuerydslImpl extends QuerydslRepositorySupport implements UpdateRequestQuerydsl {

    private Integer pageSize = 10;

    public UpdateRequestQuerydslImpl() {
        super(UpdateRequest.class);
    }


    @Override
    public Page<UpdateRequest> searchRequest(SearchRequestDto searchRequest) {
        log.info("searchRequest={}", searchRequest);
        QUpdateRequest request = QUpdateRequest.updateRequest;

        JPQLQuery<UpdateRequest> query = from(request);

        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequest.getStatus() != null) {
            builder.and(request.status.eq(searchRequest.getStatus()));
        }


        if (!searchRequest.getKeyword().isBlank()) {
            builder.and(request.content.containsIgnoreCase(searchRequest.getKeyword()));
        }

        Pageable pageable = PageRequest.of(searchRequest.getPage(), pageSize);
        query.where(builder);

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        long total = query.fetchCount();
        List<UpdateRequest> content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
