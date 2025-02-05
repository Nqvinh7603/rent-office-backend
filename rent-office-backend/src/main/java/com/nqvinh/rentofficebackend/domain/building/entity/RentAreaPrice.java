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
@Table(name = "rent_area_price_histories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentAreaPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "rent_area_price_history_id_seq")
    @SequenceGenerator(name = "rent_area_price_history_id_seq", sequenceName = "rent_area_price_histories_seq", allocationSize = 1)
    Long rentAreaPriceId;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "effective_date", nullable = false)
    LocalDate effectiveDate; // Ngày bắt đầu hiệu lực của giá này

    @ManyToOne
    @JoinColumn(name = "rent_area_id", nullable = false)
    RentArea rentArea;
}
