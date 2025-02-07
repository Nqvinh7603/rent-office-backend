package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fee_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_pricing_id_seq")
    @SequenceGenerator(name = "fee_pricing_id_seq", sequenceName = "fee_pricings_seq", allocationSize = 1)
    Long feePricingId;

    @ManyToOne
    @JoinColumn(name = "fee_id", nullable = false)
    Fee fee;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    String description;

    @Column(name = "valid_from", nullable = false)
    LocalDate validFrom;

    @Column(name = "valid_to", nullable = true)
    LocalDate validTo;
}