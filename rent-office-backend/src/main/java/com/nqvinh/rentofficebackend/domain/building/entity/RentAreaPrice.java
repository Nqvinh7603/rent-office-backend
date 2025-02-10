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
@Table(name = "rent_area_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentAreaPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "rent_area_price_id_seq")
    @SequenceGenerator(name = "rent_area_price_id_seq", sequenceName = "rent_area_prices_seq", allocationSize = 1)
    Long rentAreaPriceId;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    BigDecimal price;

    @Column(name = "valid_from", nullable = false)
    LocalDate validFrom;

    @Column(name = "valid_to", nullable = true)
    LocalDate validTo;

    @ManyToOne
    @JoinColumn(name = "rent_area_id", nullable = false)
    RentArea rentArea;
}
