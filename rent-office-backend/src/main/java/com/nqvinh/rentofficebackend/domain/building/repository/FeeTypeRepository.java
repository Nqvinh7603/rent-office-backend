/*******************************************************************************
 * Class        ：FeeTypeRepository
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * FeeTypeRepository
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface FeeTypeRepository extends JpaRepository<FeeType, Long>, JpaSpecificationExecutor<FeeType> {
}