/*******************************************************************************
 * Class        ：AppointmentBuildingRepository
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.AppointmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * AppointmentBuildingRepository
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface AppointmentBuildingRepository extends JpaRepository<AppointmentBuilding, Long>, JpaSpecificationExecutor<AppointmentBuilding> {
    @Query("""
    SELECT ab FROM AppointmentBuilding ab
    JOIN ab.appointment a
    JOIN a.customer c
    LEFT JOIN c.users cu
    LEFT JOIN ab.building b
    LEFT JOIN b.users bu
    WHERE (:isPrivileged = TRUE OR cu.userId = :userId OR bu.userId = :userId)
""")
    List<AppointmentBuilding> findAllAssignedWithPrivilege(
            @Param("userId") UUID userId,
            @Param("isPrivileged") boolean isPrivileged
    );
}