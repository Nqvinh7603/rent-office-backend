/*******************************************************************************
 * Class        ：BuildingClientService
 * Created date ：2025/03/21
 * Lasted date  ：2025/03/21
 * Author       ：vinhNQ2
 * Change log   ：2025/03/21：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;

import java.util.List;
import java.util.Map;

/**
 * BuildingClientService
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface BuildingClientService {
    Page<BuildingDto> getBuildingClients(Map<String, String> params);
    List<String> getAllStreetByWardNameAndDistrictName(String wardName, String districtName);
}