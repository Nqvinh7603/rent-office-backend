/*******************************************************************************
 * Class        ：FeeTypeService
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.FeeTypeDto;

import java.util.List;
import java.util.Map;

/**
 * FeeTypeService
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface FeeTypeService {

    FeeTypeDto createFeeType(FeeTypeDto feeTypeDto);
    FeeTypeDto updateFeeType(Long feeTypeId, FeeTypeDto feeTypeDto);
    void deleteFeeType(Long id);
    List<FeeTypeDto> getAllFeeTypes();
    Page<FeeTypeDto> getFeeTypes(Map<String, String> params);
}