/*******************************************************************************
 * Class        ：WebhookController
 * Created date ：2025/03/31
 * Lasted date  ：2025/03/31
 * Author       ：vinhNQ2
 * Change log   ：2025/03/31：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.application.api;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.infrastructure.service.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.WEB_HOOK)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class WebhookController {

    WebHookService webHookService;

    @PostMapping
    public Map<String, Object> handleDialogflowWebhook(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        Map<String, Object> intent = (Map<String, Object>) queryResult.get("intent");
        String intentName = (String) intent.get("displayName");

        Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");
        String orientation = (String) parameters.getOrDefault("orientation", "");
        String buildingLevel = (String) parameters.getOrDefault("buildingLevel", "");
        String buildingType = (String) parameters.getOrDefault("buildingType", "");
        String city = (String) parameters.getOrDefault("city", "");
        String district = (String) parameters.getOrDefault("district", "");
        String ward = (String) parameters.getOrDefault("ward", "");
        String minArea = parameters.get("minArea") != null ? parameters.get("minArea").toString() : "";
        String maxArea = parameters.get("maxArea") != null ? parameters.get("maxArea").toString() : "";
        String minPrice = parameters.get("minPrice") != null ? parameters.get("minPrice").toString() : "";
        String maxPrice = parameters.get("maxPrice") != null ? parameters.get("maxPrice").toString() : "";
        switch (intentName) {
            case "buildingLevel":
                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getAllLevelServices())));
                break;

            case "buildingType":
                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getAllTypeServices())));
                break;

            case "searchOffice":
                Map<String, String> searchParams = new HashMap<>();
                if (!orientation.isEmpty()) {
                    searchParams.put("orientation", orientation);
                }
                if (!buildingLevel.isEmpty()) {
                    searchParams.put("buildingLevel", buildingLevel);
                }
                if (!buildingType.isEmpty()) {
                    searchParams.put("buildingType", buildingType);
                }
                if (!city.isEmpty()) {
                    searchParams.put("city", city);
                }
                if (!district.isEmpty()) {
                    searchParams.put("district", district);
                }
                if (!ward.isEmpty()) {
                    searchParams.put("ward", ward);
                }
                if (!minArea.isEmpty()) {
                    searchParams.put("minArea", minArea);
                }
                if (!maxArea.isEmpty()) {
                    searchParams.put("maxArea", maxArea);
                }
                if (!minPrice.isEmpty()) {
                    searchParams.put("minPrice", minPrice);
                }
                if (!maxPrice.isEmpty()) {
                    searchParams.put("maxPrice", maxPrice);
                }

                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getBuildingClientsList(searchParams))));
                break;

            default:
                // Nếu không tìm thấy intent, trả về thông báo lỗi
                response.put("fulfillmentText", "Xin lỗi, tôi không thể hiểu yêu cầu của bạn.");
                break;
        }

        return response;
    }
}