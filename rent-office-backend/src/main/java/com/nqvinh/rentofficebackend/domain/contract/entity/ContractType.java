/*******************************************************************************
 * Class        ：ContractType
 * Created date ：2025/03/16
 * Lasted date  ：2025/03/16
 * Author       ：vinhNQ2
 * Change log   ：2025/03/16：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.contract.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * ContractType
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
@Table(name = "contract_types")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "contract_type_id_seq")
    @SequenceGenerator(name = "contract_type_id_seq", sequenceName = "contract_types_seq", allocationSize = 1)
    Long contractTypeId;

    @Column(name = "contract_type_name", nullable = false)
    String contractTypeName;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    String description;

    @OneToMany(mappedBy = "contractType",cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<Contract> contracts;
}