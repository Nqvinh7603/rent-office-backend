
/*******************************************************************************
 * Class        ：ConsignmentStatusHistory
 * Created date ：2025/03/04
 * Lasted date  ：2025/03/04
 * Author       ：vinhNQ2
 * Change log   ：2025/03/04：01-00 vinhNQ2 create a new
******************************************************************************/
package com.nqvinh.rentofficebackend.domain.customer.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import com.nqvinh.rentofficebackend.domain.customer.constant.ConsignmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * ConsignmentStatusHistory
 *
 * @version 01-00
 * @since 01-00
 * @author vinhNQ2
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consignment_status_histories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsignmentStatusHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consignment_status_history_id_seq")
    @SequenceGenerator(name = "consignment_status_history_id_seq", sequenceName = "consignment_status_histories_seq", allocationSize = 1)
    Long consignmentStatusHistoryId;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    ConsignmentStatus status;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    String note;

    @ManyToOne
    @JoinColumn(name = "consignment_id", nullable = false)
    Consignment consignment;

}