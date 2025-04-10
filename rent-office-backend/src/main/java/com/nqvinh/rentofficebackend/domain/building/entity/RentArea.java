/*******************************************************************************
 * Class        ：RentArea
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * RentArea
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rent_areas")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_area_id_seq")
    @SequenceGenerator(name = "rent_area_id_seq", sequenceName = "rent_areas_seq", allocationSize = 1)
    Long rentAreaId;

    @Column(name = "area", nullable = false)
    Integer area;

    @ManyToOne
    @JoinColumn(name = "building_unit_id", nullable = false)
    BuildingUnit buildingUnit;
}