/*******************************************************************************
 * Class        ：WebhookSerivce
 * Created date ：2025/03/31
 * Lasted date  ：2025/03/31
 * Author       ：vinhNQ2
 * Change log   ：2025/03/31：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.application.dto.RichContent;
import com.nqvinh.rentofficebackend.application.dto.RichContentItem;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingClientService;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingLevelService;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * WebhookSerivce
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class WebHookService {

    BuildingLevelService buildingLevelService;
    BuildingTypeService buildingTypeService;
    BuildingClientService buildingClientService;

    public RichContent getAllLevelServices() {
        List<BuildingLevelDto> buildingLevels = buildingLevelService.getAllBuildingLevels();


        RichContent richContent = new RichContent(List.of(
                List.of(
                        RichContentItem.builder()
                                .type("description")
                                .title("Cyber-Real phân loại văn phòng theo các hạng mục sau:")
                                .text(buildingLevels.stream()
                                        .map(BuildingLevelDto::getBuildingLevelName)
                                        .collect(Collectors.toList()))
                                .build(),
                        RichContentItem.builder()
                                .type("divider")
                                .build(),
                        RichContentItem.builder()
                                .type("description")
                                .title("Bạn muốn xem hạng mục nào?")
                                .build(),
                        RichContentItem.builder()
                                .type("chips")
                                .options(buildingLevels.stream()
                                        .map(level -> new RichContentItem.Option(level.getBuildingLevelName()))
                                        .collect(Collectors.toList()))
                                .build()
                )
        ));
        return richContent;
    }


    public RichContent getAllTypeServices() {
        List<BuildingTypeDto> buildingTypes = buildingTypeService.getAllBuildingTypes();


        RichContent richContent = new RichContent(List.of(
                List.of(
                        RichContentItem.builder()
                                .type("description")
                                .title("Cyber-Real hiện tại có những loại văn phòng sau:")
                                .text(buildingTypes.stream()
                                        .map(BuildingTypeDto::getBuildingTypeName)
                                        .collect(Collectors.toList()))
                                .build(),
                        RichContentItem.builder()
                                .type("divider")
                                .build(),
                        RichContentItem.builder()
                                .type("description")
                                .title("Bạn muốn xem loại văn phòng nào?")
                                .build(),
                        RichContentItem.builder()
                                .type("chips")
                                .options(buildingTypes.stream()
                                        .map(level -> new RichContentItem.Option(level.getBuildingTypeName()))
                                        .collect(Collectors.toList()))
                                .build()
                )
        ));
        return richContent;


    }

    public RichContent getBuildingClientsList(Map<String, String> params) {

        List<BuildingDto> buildingClients = buildingClientService.getBuildingClientsList(params);
        if (buildingClients.isEmpty()) {
            return new RichContent(List.of(
                    List.of(
                            RichContentItem.builder()
                                    .type("description")
                                    .title("Không tìm thấy văn phòng nào phù hợp với yêu cầu của bạn.")
                                    .build()
                    )
            ));
        }
        RichContent richContent = new RichContent(List.of(
                List.of(
                        RichContentItem.builder()
                                .type("description")
                                .title("Danh sách văn phòng phù hợp:")
                                .text(buildingClients.stream()
                                        .map(BuildingDto::getBuildingName)
                                        .collect(Collectors.toList()))
                                .build(),
                        RichContentItem.builder()
                                .type("divider")
                                .build(),
                        RichContentItem.builder()
                                .type("description")
                                .title("Bạn muốn xem chi tiết văn phòng nào?")
                                .build(),
                        RichContentItem.builder()
                                .type("chips")
                                .options(buildingClients.stream()
                                        .map(building -> new RichContentItem.Option(building.getBuildingName()))
                                        .collect(Collectors.toList()))
                                .build()
                )
        ));
        return richContent;
    }

//    public RichContent getChildCategories(String parentCategoryName) {
//        String slug = formatSlugify(parentCategoryName);
//        List<String> childCategoryNames = categoryService.getChildCategoryNames(slug);
//
//        RichContent richContent = new RichContent(List.of(
//                List.of(
//                        RichContentItem.builder()
//                                .type("description")
//                                .title("Danh mục con của " + parentCategoryName + " bao gồm:")
//                                .text(String.join("\r\n", childCategoryNames))
//                                .build(),
//                        RichContentItem.builder()
//                                .type("divider")
//                                .build(),
//                        RichContentItem.builder()
//                                .type("description")
//                                .title("Bạn muốn xem danh mục con nào?")
//                                .build(),
//                        RichContentItem.builder()
//                                .type("chips")
//                                .options(childCategoryNames.stream().map(name -> new RichContentItem.Option(name)).collect(Collectors.toList()))
//                                .build()
//                )
//        ));
//        return richContent;
//    }
//
//    public RichContent getProductsOfChildCategory(String childCategoryName) {
//        String slug = formatSlugify(childCategoryName);
//        List<String> productNames = productService.getProductNamesByCategory(slug, ProductType.TRENDING);
//
//        RichContent richContent = new RichContent(List.of(
//                List.of(
//                        RichContentItem.builder()
//                                .type("description")
//                                .title("Một số sản phẩm bán chạy thuộc danh mục " + childCategoryName + " bao gồm:")
//                                .text(String.join("\r\n", productNames))
//                                .build(),
//                        RichContentItem.builder()
//                                .type("chips")
//                                .options(productNames.stream().map(name -> new RichContentItem.Option(name)).collect(Collectors.toList()))
//                                .build()
//                )
//        ));
//        return richContent;
//    }
//
//    public RichContent getPolicies() {
//        RichContent richContent = new RichContent(List.of(
//                List.of(
//                        RichContentItem.builder()
//                                .type("description")
//                                .title("Để xem chính sách, vui lòng truy cập vào đường link sau:")
//                                .build(),
//                        RichContentItem.builder()
//                                .type("info")
//                                .title("Chính sách")
//                                .subtitle("Chính sách của Decorpic")
//                                .image(new RichContentItem.Image("/logo.png"))
//                                .actionLink(System.getenv("FRONTEND_URL") + "/chinh-sach-thanh-toan")
//                                .build()
//                )
//        ));
//        return richContent;
//    }
//
//    private String formatSlugify(String input) {
//        return input.toLowerCase().replace(" ", "-");
//    }


}