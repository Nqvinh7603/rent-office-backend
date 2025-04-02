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
                                        .map(level -> new RichContentItem.Option(level.getBuildingLevelName(), null))
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
                                        .map(level -> new RichContentItem.Option(level.getBuildingTypeName(), null))
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
        if (buildingClients.size() == 1) {
            BuildingDto building = buildingClients.getFirst();
            return new RichContent(List.of(
                    List.of(
                            RichContentItem.builder()
                                    .type("image")
                                    .rawUrl(building.getBuildingImages().getFirst().getImgUrl())
                                    .build(),
                            RichContentItem.builder()
                                    .type("info")
                                    .title(building.getBuildingName())
                                    .subTitle(building.getBuildingNumber() + ", " + building.getStreet() + ", " + building.getWard() + ", " + building.getDistrict())
                                    .actionLink("http://localhost:5173/van-phong/" + building.getBuildingId())
                                    .build(),
                            RichContentItem.builder()
                                    .type("chips")
                                    .options(List.of(
                                            new RichContentItem.Option(
                                                    "Xem chi tiết",
                                                    "http://localhost:5173/van-phong/" + building.getBuildingId()
                                            )
                                    ))
                                    .build()
                    )
            ));
        }
        else {
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
                                            .map(building -> new RichContentItem.Option(
                                                    building.getBuildingName(),
                                                    "http://localhost:5173/van-phong/" + building.getBuildingId()
                                            ))
                                            .collect(Collectors.toList()))
                                    .build()
                    )
            ));

            return richContent;
        }

    }


//
//    private String formatSlugify(String input) {
//        return input.toLowerCase().replace(" ", "-");
//    }


}