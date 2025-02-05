package com.nqvinh.rentofficebackend.infrastructure.utils;

import com.nqvinh.rentofficebackend.application.dto.request.SearchCriteria;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RequestParamUtils {

    EntityManager entityManager;

//    public List<Sort.Order> toSortOrders(Map<String, String> params, Class<?> entityClass) {
//        List<Sort.Order> sortOrders = new ArrayList<>();
//        String sortBy = params.getOrDefault("sortBy", "createdAt");
//        String direction = params.getOrDefault("direction", "desc");
//        String idFieldName = EntityUtils.getIdFieldName(entityManager, entityClass);
//
//        if (sortBy != null && !sortBy.isEmpty()) {
//            Sort.Order createdAtOrder = new Sort.Order(Sort.Direction.fromString(direction), sortBy);
//            Sort.Order updatedAtOrder = new Sort.Order(Sort.Direction.fromString("asc"), "updatedAt");
//            Sort.Order idOrder = new Sort.Order(Sort.Direction.ASC, idFieldName);
//            sortOrders.addAll(List.of(createdAtOrder, updatedAtOrder, idOrder));
//        }
//        return sortOrders;
//    }

    public List<Sort.Order> toSortOrders(Map<String, String> params) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        String sortBy = params.getOrDefault("sortBy", "latestAt");
        String direction = params.getOrDefault("direction", "desc");

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), sortBy);
            sortOrders.add(sortOrder);
        }

        if (!"latestAt".equals(sortBy)) {
            Sort.Order latestAtOrder = new Sort.Order(Sort.Direction.DESC, "latestAt");
            sortOrders.add(latestAtOrder);
        }

        return sortOrders;
    }



    public List<SearchCriteria> getSearchCriteria(Map<String, String> params, String key) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        String value = params.get(key);
        if(value != null && !value.isEmpty()) {
            String[] valueArr = value.split(",");
            for (String searchValue : valueArr) {
                SearchCriteria searchCriteria = SearchCriteria.builder()
                        .key(key)
                        .operation("=")
                        .value(searchValue)
                        .build();
                searchCriteriaList.add(searchCriteria);
            }
        }
        return searchCriteriaList;
    }
}

//package com.nqvinh.rentofficebackend.infrastructure.utils;
//
//import com.nqvinh.rentofficebackend.application.dto.request.SearchCriteria;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class RequestParamsUtils {
//
//    private RequestParamsUtils() {
//    }
//
//    public static PaginationParams getPaginationParams(Map<String, String> requestParams) {
//        int page = Integer.parseInt(requestParams.getOrDefault("page", "1"));
//        int pageSize = Integer.parseInt(requestParams.getOrDefault("pageSize", "10"));
//        requestParams.remove("page");
//        requestParams.remove("pageSize");
//        return PaginationParams.builder()
//                .page(page)
//                .pageSize(pageSize)
//                .build();
//    }
//
//    public static SortParams getSortParams(Map<String, String> requestParams) {
//        String sortBy = requestParams.getOrDefault("sortBy", "createdAt");
//        String direction = requestParams.getOrDefault("direction", "desc");
//        requestParams.remove("sortBy");
//        requestParams.remove("direction");
//        return SortParams.builder()
//                .sortBy(sortBy)
//                .direction(direction)
//                .build();
//    }
//}
//
