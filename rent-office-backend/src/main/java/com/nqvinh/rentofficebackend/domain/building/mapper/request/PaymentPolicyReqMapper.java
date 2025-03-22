/*******************************************************************************
 * Class        ：PaymentPolicyReqMapper
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.PaymentPolicyReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.PaymentPolicy;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * PaymentPolicyReqMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface PaymentPolicyReqMapper extends CommonMapper<PaymentPolicyReqDto, PaymentPolicy> {
}