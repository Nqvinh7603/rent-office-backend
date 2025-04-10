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
@Table(name = "building_levels")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingLevel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_level_id_seq")
    @SequenceGenerator(name = "building_level_id_seq", sequenceName = "building_level_id_seq", allocationSize = 1)
    Long buildingLevelId;

    @Column(name = "building_level_code", nullable = false, unique = true)
    String buildingLevelCode;

    @Column(name = "building_level_name", nullable = false)
    String buildingLevelName;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "buildingLevel", cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<Building> buildings;
}
