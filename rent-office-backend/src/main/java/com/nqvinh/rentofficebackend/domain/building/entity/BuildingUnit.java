/*******************************************************************************
 * Class        ：BuildingUnit
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.constant.BuildingUnitStatus;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * BuildingUnit
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
@Table(name = "building_units")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingUnit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "building_unit_id_seq")
    @SequenceGenerator(name = "building_unit_id_seq", sequenceName = "building_units_seq", allocationSize = 1)
    Long buildingUnitId;

    @Column(name = "floor", nullable = false)
    Integer floor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    BuildingUnitStatus buildingUnitStatus;

    @Column(name = "unit_name", nullable = true)
    String unitName;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @OneToMany(mappedBy = "buildingUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RentArea> rentAreas;

}