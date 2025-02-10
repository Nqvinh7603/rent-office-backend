package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.constant.BuildingStatusEnum;
import com.nqvinh.rentofficebackend.domain.building.constant.OrientationEnum;
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
@Table(name = "buildings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "building_id_seq")
    @SequenceGenerator(name = "building_id_seq", sequenceName = "buildings_seq", allocationSize = 1)
    Long buildingId;

    @Column(name = "building_name", nullable = false)
    String buildingName;

    @Column(name = "ward", nullable = true)
    String ward; // phường

    @Column(name = "district", nullable = true)
    String district; // quận

    @Column(name = "city", nullable = true)
    String city; // thành phố

    @Column(name = "street", nullable = true)
    String street; // địa chỉ

    @Column(name = "building_number", nullable = true)
    String buildingNumber;

    @Column(name = "working_hours", columnDefinition = "TEXT", nullable = true)
    String workingHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "orientation", nullable = true)
    OrientationEnum orientation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BuildingStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "building_level_id", nullable = false)
    BuildingLevel buildingLevel;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<BuildingImage> buildingImages;

    @ManyToOne
    @JoinColumn(name = "building_type_id", nullable = false)
    BuildingType buildingType;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<BuildingUnit> buildingUnits;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<FeePrice> feePrices;

}
