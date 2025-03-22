/*******************************************************************************
 * Class        ：Contract
 * Created date ：2025/03/15
 * Lasted date  ：2025/03/15
 * Author       ：vinhNQ2
 * Change log   ：2025/03/15：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.contract.entity;

import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import com.nqvinh.rentofficebackend.domain.contract.constant.ContractStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract
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
@Table(name = "contracts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_id_seq")
    @SequenceGenerator(name = "contract_id_seq", sequenceName = "contract_seq", allocationSize = 1)
    Long contractId;

    @ManyToOne
    @JoinColumn(name = "contract_type_id", nullable = false)
    ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    ContractStatus status;

    @Column(name = "end_date", nullable = false)
    LocalDateTime endDate;

    @Lob
    @Column(name = "customer_signature", nullable = true)
    byte[] customerSignature;

    @Lob
    @Column(name = "staff_signature", nullable = true)
    byte[] staffSignature;

    @Column(name = "commission_fee", nullable = true)
    BigDecimal commissionFee;

    @OneToMany(mappedBy = "contract", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<PaymentTerm> paymentTerms;
}
