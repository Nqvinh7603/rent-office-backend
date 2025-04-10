/*******************************************************************************
 * Class        ：PaymentPolicy
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

/**
 * PaymentPolicy
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
@Table(name = "payment_policy")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentPolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "payment_policy_id_seq")
    @SequenceGenerator(name = "payment_policy_id_seq", sequenceName = "payment_policy_seq", allocationSize = 1)
    Long paymentPolicyId;

    @Column(name = "deposit_term", nullable = false)
    Integer depositTerm; // Số tháng tiền đặt cọc

    @Column(name = "payment_cycle", nullable = false)
    String paymentCycle; // Tháng, Quý, Năm

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;
}