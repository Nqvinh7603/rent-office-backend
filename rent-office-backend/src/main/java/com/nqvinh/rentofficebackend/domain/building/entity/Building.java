package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.building.constant.BuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.OrientationEnum;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @Column(name = "building_name", nullable = true)
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

    @Column(name = "orientation", nullable = true )
    @Enumerated(EnumType.STRING)
    OrientationEnum orientation; // Hướng

    @OneToMany(mappedBy = "building",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    List<BuildingImage>  buildingImages = new ArrayList<>();

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    String description;

    @Column(name = "building_status", nullable = true)
    @Enumerated(EnumType.STRING)
    BuildingStatus buildingStatus;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ConsignmentStatusHistory> consignmentStatusHistories;

    @OneToMany(mappedBy = "building",fetch = FetchType.EAGER ,cascade = CascadeType.ALL, orphanRemoval = true)
    List<Fee> fees;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_type_id", nullable = true)
    BuildingType buildingType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "building_level_id", nullable = true)
    BuildingLevel buildingLevel;

//    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    List<RentalPricing> rentalPricing;

    @Column(name = "number_of_floors", nullable = true)
    Integer numberOfFloors;

    @Column(name = "total_area", nullable = true)
    Integer totalArea;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<PaymentPolicy> paymentPolicies;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BuildingDetail> buildingDetails;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BuildingUnit> buildingUnits;

//    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
//    List<Contract> contracts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assign_building",
            joinColumns = @JoinColumn(name = "building_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "buildings")
    List<Appointment> appointments;
}
