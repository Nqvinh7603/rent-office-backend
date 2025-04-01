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
//        String orientation = (String) parameters.get("orientation");
        String buildingLevel = (String) parameters.get("buildingLevel");
        String buildingType = (String) parameters.get("buildingType");
//        String district = (String) parameters.get("district");
//        String city = (String) parameters.get("city");


        switch (intentName) {
            case "buildingLevel":
                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getAllLevelServices())));
                break;
            case "buildingType":
                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getAllTypeServices())));
                break;
            case "searchOffice":
                // Giả sử intent "searchOffice" sẽ tìm văn phòng theo các tham số đã cung cấp
                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getBuildingClientsList(
                        Map.of(
                                "buildingLevel", buildingLevel,
                                "buildingType", buildingType
                        )
                ))));
                break;
//            case "GetChildCategories":
//                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getChildCategories((String) queryResult.get("parameters")))));
//                break;
//            case "GetProductsOfChildCategory":
//                response = Map.of("fulfillmentMessages", List.of(Map.of("payload", webHookService.getProductsOfChildCategory((String) queryResult.get("parameters")))));
//                break;
//            default:
//                response.put("fulfillmentText", "Sorry, I couldn't understand your request.");
            default:
                response.put("fulfillmentText", "Xin lỗi tôi không thể hiểu yêu cầu của bạn.");
                break;
        }

        return response;
    }
}