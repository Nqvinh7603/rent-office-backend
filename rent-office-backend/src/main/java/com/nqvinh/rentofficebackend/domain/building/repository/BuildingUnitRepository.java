/*******************************************************************************
 * Class        ：BuildingUnitRepository
 * Created date ：2025/03/22
 * Lasted date  ：2025/03/22
 * Author       ：vinhNQ2
 * Change log   ：2025/03/22：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.BuildingUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BuildingUnitRepository
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface BuildingUnitRepository extends JpaRepository<BuildingUnit, Long> {
}