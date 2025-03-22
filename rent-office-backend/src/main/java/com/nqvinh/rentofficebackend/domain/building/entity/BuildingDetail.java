/*******************************************************************************
 * Class        ：BuildingSection
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * BuildingSection
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
@Table(name = "building_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_detail_id_seq")
    @SequenceGenerator(name = "building_detail_id_seq", sequenceName = "building_detail_seq", allocationSize = 1)
    Long buildingDetailId;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

    @Column(name = "section", nullable = true)
    String section;

    @Column(name = "content", nullable = true, columnDefinition = "TEXT")
    String content;
}