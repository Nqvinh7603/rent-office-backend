/*******************************************************************************
 * Class        ：Appointment
 * Created date ：2025/03/21
 * Lasted date  ：2025/03/21
 * Author       ：vinhNQ2
 * Change log   ：2025/03/21：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Appointment
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
@Table(name = "appointments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "appointment_id_seq")
    @SequenceGenerator(name = "appointment_id_seq", sequenceName = "appointment_id_seq", allocationSize = 1)
    Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToMany(mappedBy = "appointment",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    List<AppointmentBuilding> appointmentBuildings;

    @OneToMany(mappedBy = "appointment",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    List<AppointmentStatusHistory> appointmentStatusHistories;
}