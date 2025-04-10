/*******************************************************************************
 * Class        ：AppointmentRepository
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * AppointmentRepository
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
}