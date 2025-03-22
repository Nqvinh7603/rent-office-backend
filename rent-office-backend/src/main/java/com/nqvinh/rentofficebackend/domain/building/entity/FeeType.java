/*******************************************************************************
 * Class        ：FeeType
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * FeeType
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
@Table(name = "fee_types")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_type_id_seq")
    @SequenceGenerator(name = "fee_type_id_seq", sequenceName = "fee_type_seq", allocationSize = 1)
    Long feeTypeId;

    @Column(name = "fee_type_name", nullable = false)
    String feeTypeName;

    @OneToMany(mappedBy = "feeType", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<Fee> fees;
}