package com.nqvinh.rentofficebackend.domain.customer.entity;

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
@Table(name = "consignment_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsignmentImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "consignment_image_id_seq")
    @SequenceGenerator(name = "consignment_image_id_seq", sequenceName = "consignment_images_seq", allocationSize = 1)
    Long consignmentImageId;

    @Column(name = "img_url", nullable = false)
    String imgUrl;

    @ManyToOne
    @JoinColumn(name = "consignment_id", nullable = false)
    Consignment consignment;
}
