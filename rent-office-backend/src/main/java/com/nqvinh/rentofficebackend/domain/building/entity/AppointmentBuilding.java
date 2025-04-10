/*******************************************************************************
 * Class        ：AppointmentBuilding
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AppointmentBuilding
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
@Table(name = "appointment_building")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentBuilding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "appointment_building_id_seq")
    @SequenceGenerator(name = "appointment_building_id_seq", sequenceName = "appointment_building_id_seq", allocationSize = 1)
    Long appointmentBuildingId;

    @OneToMany(mappedBy = "appointmentBuilding", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<AppointmentBuildingStatusHistory> appointmentBuildingStatusHistories;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = true)
    Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = true)
    Building building;

    @Column(name = "visit_time", nullable = true)
    LocalDateTime visitTime;

    @Column(name = "area", nullable = true)
    String area;
}