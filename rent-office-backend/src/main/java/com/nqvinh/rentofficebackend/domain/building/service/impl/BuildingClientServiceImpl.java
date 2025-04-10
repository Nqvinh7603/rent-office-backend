/*******************************************************************************
 * Class        ：BuildingClientServiceImpl
 * Created date ：2025/03/21
 * Lasted date  ：2025/03/21
 * Author       ：vinhNQ2
 * Change log   ：2025/03/21：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.building.constant.OrientationEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingClientService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * BuildingClientServiceImpl
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingClientServiceImpl implements BuildingClientService {

    BuildingRepository buildingRepository;
    BuildingMapper buildingMapper;
    PaginationUtils paginationUtils;
    StringUtils stringUtils;
    @Override
    public Page<BuildingDto> getBuildingClients(Map<String, String> params) {

        Specification<Building> spec = getBuildingSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Building> buildingPage = buildingRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(buildingPage, pageable);
        return paginationUtils.mapPage(buildingPage, meta, buildingMapper::toDto);
    }


    @Override
    public List<BuildingDto> getBuildingClientsList(Map<String, String> params) {
        Specification<Building> spec = getBuildingSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        List<Building> buildings = buildingRepository.findAll(spec);
        return buildingMapper.toDtoList(buildings);
    }

    @Override
    public List<String> getAllStreetByWardNameAndDistrictName(String ward, String district) {
        return buildingRepository.findDistinctStreetsByWardAndDistrict(ward, district);
    }

    private Specification<Building> getBuildingSpec(Map<String, String> params) {
        Specification<Building> spec = (root, query, cb) -> cb.isNotNull(root.get("buildingStatus"));

        spec = spec.and((root, query, cb) -> cb.equal(root.get("buildingStatus"), "AVAILABLE"));


        if (params.containsKey("orientation")) {
            String orientation = stringUtils.normalizeString(params.get("orientation").trim().toUpperCase());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("orientation"), orientation)
            );
        }

        if (params.containsKey("email")) {
            String email = stringUtils.normalizeString(params.get("email").trim().toLowerCase());
            String likePattern = "%" + email + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customer").get("email"))), likePattern)
            );
        }

        if (params.containsKey("customerName")) {
            String customerName = stringUtils.normalizeString(params.get("customerName").trim().toLowerCase());
            String likePattern = "%" + customerName + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customer").get("customerName"))), likePattern)
            );
        }

        if (params.containsKey("buildingName")) {
            String customerName = stringUtils.normalizeString(params.get("buildingName").trim().toLowerCase());
            String likePattern = "%" + customerName + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingName"))), likePattern)
            );
        }

        if (params.containsKey("phoneNumber")) {
            String phoneNumber = stringUtils.normalizeString(params.get("phoneNumber").trim().toLowerCase());
            spec = spec.and((root, query, cb) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                return cb.equal(customerJoin.get("phoneNumber"), phoneNumber);
            });
        }

        if (params.containsKey("buildingType")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingType").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingType").get("buildingTypeName"))), likePattern)
            );
        }

        if (params.containsKey("buildingLevel")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingLevel").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingLevel").get("buildingLevelName"))), likePattern)
            );
        }

        if (params.containsKey("district")) {
            String district = stringUtils.normalizeString(params.get("district").trim().toLowerCase());
            String likePattern = "%" + district + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("district"))), likePattern)
            );
        }
        if (params.containsKey("city")) {
            String city = stringUtils.normalizeString(params.get("city").trim().toLowerCase());
            String likePattern = "%" + city + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("city"))), likePattern)
            );
        }

        if (params.containsKey("ward")) {
            String ward = stringUtils.normalizeString(params.get("ward").trim().toLowerCase());
            String likePattern = "%" + ward.replaceAll("\\s+", "%") + "%"; // Thay khoảng trắng bằng % để tìm kiếm linh hoạt
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("ward"))),
                            likePattern
                    )
            );
        }

        if (params.containsKey("street")) {
            String street = stringUtils.normalizeString(params.get("street").trim().toLowerCase());
            String likePattern = "%" + street.replaceAll("\\s+", "%") + "%"; // Thay khoảng trắng bằng % để tìm kiếm linh hoạt
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("street"))),
                            likePattern
                    )
            );
        }

        if (params.containsKey("minPrice") || params.containsKey("maxPrice")) {
            if (params.containsKey("minPrice")) {
                BigDecimal minPrice = new BigDecimal(params.get("minPrice"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<BuildingUnit> buildingUnitRoot = subquery.from(BuildingUnit.class);
                    Join<BuildingUnit, RentalPricing> rentalPricingJoin = buildingUnitRoot.join("rentalPricing");

                    subquery.select(criteriaBuilder.max(rentalPricingJoin.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(buildingUnitRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.greaterThanOrEqualTo(priceSubquery, minPrice);
                });
            }


            if (params.containsKey("maxPrice")) {
                BigDecimal maxPrice = new BigDecimal(params.get("maxPrice"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<BuildingUnit> buildingUnitRoot = subquery.from(BuildingUnit.class);
                    Join<BuildingUnit, RentalPricing> rentalPricingJoin = buildingUnitRoot.join("rentalPricing");

                    subquery.select(criteriaBuilder.max(rentalPricingJoin.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(buildingUnitRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.lessThanOrEqualTo(priceSubquery, maxPrice);
                });
            }
        }

        if (params.containsKey("minArea") || params.containsKey("maxArea")) {
            if (params.containsKey("minArea")) {
                BigDecimal minPrice = new BigDecimal(params.get("minArea"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<BuildingUnit> buildingUnitRoot = subquery.from(BuildingUnit.class);
                    Join<BuildingUnit, RentArea> rentalPricingJoin = buildingUnitRoot.join("rentAreas");

                    subquery.select(criteriaBuilder.max(rentalPricingJoin.get("rentAreaId")))
                            .where(criteriaBuilder.equal(buildingUnitRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentArea> rentalPricingRoot2 = priceSubquery.from(RentArea.class);
                    priceSubquery.select(rentalPricingRoot2.get("area"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentAreaId"), subquery));

                    return criteriaBuilder.greaterThanOrEqualTo(priceSubquery, minPrice);
                });
            }


            if (params.containsKey("maxArea")) {
                BigDecimal maxPrice = new BigDecimal(params.get("maxArea"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<BuildingUnit> buildingUnitRoot = subquery.from(BuildingUnit.class);
                    Join<BuildingUnit, RentArea> rentalPricingJoin = buildingUnitRoot.join("rentAreas");

                    subquery.select(criteriaBuilder.max(rentalPricingJoin.get("rentAreaId")))
                            .where(criteriaBuilder.equal(buildingUnitRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentArea> rentalPricingRoot2 = priceSubquery.from(RentArea.class);
                    priceSubquery.select(rentalPricingRoot2.get("area"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentAreaId"), subquery));

                    return criteriaBuilder.lessThanOrEqualTo(priceSubquery, maxPrice);
                });
            }
        }


        if (params.containsKey("staffName")) {
            String staffName = stringUtils.normalizeString(params.get("staffName").trim().toLowerCase());
            String[] nameParts = staffName.split("\\s+");

            String likePatternFirstName = nameParts.length > 1
                    ? "%" + String.join(" ", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1)) + "%"
                    : "%" + nameParts[0] + "%";
            String likePatternLastName = "%" + nameParts[nameParts.length - 1] + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.and(
                        criteriaBuilder.isNotNull(userJoin.get("userId")),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("firstName"))), likePatternFirstName),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("lastName"))), likePatternLastName)
                );
            });
        }

        if (params.containsKey("buildingStatus")) {
            String buildingStatus = params.get("buildingStatus").trim().toUpperCase();
            spec = spec.and((root, query, cb) -> cb.equal(root.get("buildingStatus"), buildingStatus));
        }
        return spec;
    }
}