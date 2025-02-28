package com.nqvinh.rentofficebackend.domain.customer.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import com.nqvinh.rentofficebackend.domain.customer.constant.ConsignmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consignments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consignment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "consignment_id_seq")
    @SequenceGenerator(name = "consignment_id_seq", sequenceName = "consignments_seq", allocationSize = 1)
    Long consignmentId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @Column(name = "ward", nullable = true)
    String ward; // phường

    @Column(name = "district", nullable = true)
    String district; // quận

    @Column(name = "city", nullable = true)
    String city; // thành phố

    @Column(name = "street", nullable = true)
    String street; // địa chỉ

    @OneToMany(mappedBy = "consignment",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    List<ConsignmentImage>  consignmentImages;

    @Column(name = "price", nullable = true, precision = 15, scale = 2)
    BigDecimal price;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    String description;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    ConsignmentStatus status;

    @Column(name = "building_type", nullable = true)
    String buildingType;


    @Column(name = "rejected_reason", nullable = true, columnDefinition = "TEXT")
    String rejectedReason;

    @Column(name = "rejected_reason_at", nullable = true)
    LocalDateTime rejectedReasonAt;

    @Column(name = "additional_info", nullable = true, columnDefinition = "TEXT")
    String additionalInfo;

    @Column(name = "additional_info_at", nullable = true)
    LocalDateTime additionalInfoAt;

    @Column(name = "confirmed_at", nullable = true)
    LocalDateTime confirmedAt;

    @Column(name = "additional_info_after_at", nullable = true)
    LocalDateTime additionalInfoAfterAt;

}
