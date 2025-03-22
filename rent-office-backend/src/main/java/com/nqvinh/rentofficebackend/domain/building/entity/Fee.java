/*******************************************************************************
 * Class        ：Fee
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

import java.util.List;

/**
 * Fee
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
@Table(name = "fees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_id_seq")
    @SequenceGenerator(name = "fee_id_seq", sequenceName = "fee_seq", allocationSize = 1)
    Long feeId;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @ManyToOne
    @JoinColumn(name = "fee_type_id", nullable = false)
    FeeType feeType;

    @OneToMany(mappedBy = "fee", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<FeePricing> feePricing;
}
