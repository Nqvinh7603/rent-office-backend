package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    @SequenceGenerator(name = "building_unit_id_seq", sequenceName = "building_unit_seq", allocationSize = 1)
    Long buildingUnitId;

    @Column(name = "floor", nullable = false)
    Integer floor;

    @Column(name = "unit_name", nullable = false)
    String buildingUnitName;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @OneToMany(mappedBy = "buildingUnit", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<RentArea> rentAreas;
}
