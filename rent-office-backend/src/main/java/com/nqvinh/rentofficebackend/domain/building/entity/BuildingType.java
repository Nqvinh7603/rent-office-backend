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
@Table(name = "building_types")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "building_type_id_seq")
    @SequenceGenerator(name = "building_type_id_seq", sequenceName = "building_types_seq", allocationSize = 1)
    Long buildingTypeId;

    @Column(name = "building_type_name", nullable = false)
    String buildingTypeName;

    @Column(name = "building_type_code", nullable = false, unique = true)
    String buildingTypeCode;

    @Column(name = "description", nullable = true)
    String description;

    @OneToMany(mappedBy = "buildingType", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<Building> buildings;

}
