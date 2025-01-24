package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "building_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "building_image_id_seq")
    @SequenceGenerator(name = "building_image_id_seq", sequenceName = "building_images_seq", allocationSize = 1)
    Long buildingImageId;

    @Column(name = "img_url", nullable = true)
    String imgUrl;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building;

}
