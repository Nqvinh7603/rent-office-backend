package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.constant.BuildingStatusEnum;
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
@Table(name = "rent_areas")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "rent_area_id_seq")
    @SequenceGenerator(name = "rent_area_id_seq", sequenceName = "rent_area_seq", allocationSize = 1)
    Long rentAreaId;

    @Column(name = "area", nullable = false)
    Double area;

    @Column(name = "seat_quantity", nullable = false)
    Integer seatQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    BuildingStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "building_unit_id", nullable = false)
    BuildingUnit buildingUnit;

    @OneToMany(mappedBy = "rentArea", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    List<RentAreaPrice> rentAreaPrices;
}
