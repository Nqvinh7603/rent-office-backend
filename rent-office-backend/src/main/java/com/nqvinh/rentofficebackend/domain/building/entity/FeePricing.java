/*******************************************************************************
 * Class        ：FeePricing
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
 * FeePricing
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
@Table(name = "fee_pricings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePricing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_pricing_id_seq")
    @SequenceGenerator(name = "fee_pricing_id_seq", sequenceName = "fee_pricing_seq", allocationSize = 1)
    Long feePricingId;

    @Column(name = "price_unit", nullable = true)
    String priceUnit;

    @Column(name = "price_value", nullable = true)
    BigDecimal priceValue;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "fee_id", nullable = false)
    Fee fee;
}