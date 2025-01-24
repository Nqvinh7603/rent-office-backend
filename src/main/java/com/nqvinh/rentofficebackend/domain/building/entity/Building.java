package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.enums.OfficeStatusEnum;
import com.nqvinh.rentofficebackend.domain.building.enums.OrientationEnum;
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

    @Column(name = "address", nullable = true)
    String ward;

    @Column(name = "district", nullable = true)
    String district;

    @Column(name = "city", nullable = true)
    String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "orientation", nullable = true)
    OrientationEnum orientation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OfficeStatusEnum status;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<BuildingImage> buildingImages;

    @ManyToOne
    @JoinColumn(name = "building_type_id", nullable = false)
    BuildingType buildingType;

}
