package com.nqvinh.rentofficebackend.infrastructure.utils;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PaginationUtils {

    RequestParamUtils requestParamUtils;

    public Pageable buildPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    public <T, D> Page<D> mapPage(org.springframework.data.domain.Page<T> entityPage, Meta meta, Function<T, D> mapper) {
        return Page.<D>builder()
                .meta(meta)
                .content(entityPage.getContent().stream().map(mapper).collect(Collectors.toList()))
                .build();
    }

    public Meta buildMeta(org.springframework.data.domain.Page<?> page, Pageable pageable) {
        return Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();
    }
}