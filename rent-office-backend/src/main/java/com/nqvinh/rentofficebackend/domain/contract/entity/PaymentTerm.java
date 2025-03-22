/*******************************************************************************
 * Class        ：PaymentTerm
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.contract.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * PaymentTerm
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
@Table(name = "payment_terms")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTerm {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "payment_term_id_seq")
    @SequenceGenerator(name = "payment_term_id_seq", sequenceName = "payment_terms_seq", allocationSize = 1)
    Long paymentTermId;

    @Column(name = "term_name", nullable = false)
    String termName;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    Contract contract;


}