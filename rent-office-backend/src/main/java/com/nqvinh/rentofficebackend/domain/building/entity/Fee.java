package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_id_seq")
    @SequenceGenerator(name = "fee_id_seq", sequenceName = "fees_seq", allocationSize = 1)
    Long feeId;

    @Column(name = "fee_name", nullable = false)
    String feeName; // Tên loại phí (ví dụ: Phí quản lý)

    @Column(name = "fee_unit", nullable = true)
    String feeUnit; // Đơn vị tính phí (ví dụ: $/m²/tháng)

    @OneToMany(mappedBy = "fee", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<FeePrice> feePrices; // Liên kết với các giá phí
}