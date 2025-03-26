/*******************************************************************************
 * Class        ：AppointmentStatusHistory
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentStatus;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * AppointmentStatusHistory
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
@Table(name = "appointment_status_histories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentStatusHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "appointment_status_history_id_seq")
    @SequenceGenerator(name = "appointment_status_history_id_seq", sequenceName = "appointment_status_history_id_seq", allocationSize = 1)
    Long appointmentStatusHistoryId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    String note;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    Appointment appointment;
}