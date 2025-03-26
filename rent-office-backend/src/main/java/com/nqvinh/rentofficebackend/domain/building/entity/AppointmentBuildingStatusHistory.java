/*******************************************************************************
 * Class        ：AppointmentBuildingStatusHistory
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentBuildingStatus;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * AppointmentBuildingStatusHistory
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
@Table(name = "appointment_building_status_histories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentBuildingStatusHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "appointment_building_status_history_id_seq")
    @SequenceGenerator(name = "appointment_building_status_history_id_seq", sequenceName = "appointment_building_status_history_id_seq", allocationSize = 1)
    Long appointmentBuildingStatusHistoryId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    AppointmentBuildingStatus status;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    String note;

    @ManyToOne
    @JoinColumn(name = "appointment_building_id", nullable = false)
    AppointmentBuilding appointmentBuilding;

}