/*******************************************************************************
 * Class        ：rentalPricing
 * Created date ：2025/03/11
 * Lasted date  ：2025/03/11
 * Author       ：vinhNQ2
 * Change log   ：2025/03/11：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * rentalPricing
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_pricing")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentalPricing extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rental_pricing_id_seq")
    @SequenceGenerator(name = "rental_pricing_id_seq", sequenceName = "rental_pricing_seq", allocationSize = 1)
    Long rentalPricingId;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;
}