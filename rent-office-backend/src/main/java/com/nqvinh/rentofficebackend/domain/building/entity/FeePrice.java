package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fee_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeePrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_pricing_id_seq")
    @SequenceGenerator(name = "fee_pricing_id_seq", sequenceName = "fee_pricings_seq", allocationSize = 1)
    Long feePricingId;

    @ManyToOne
    @JoinColumn(name = "fee_id", nullable = false)
    Fee fee;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    Building building; // Liên kết tới tòa nhà

    @Column(name = "price", nullable = true)
    BigDecimal price; // Giá trị của phí (ví dụ: 7$/m²/tháng)

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    String description; // Mô tả bổ sung (ví dụ: "Thỏa thuận" hoặc "Theo giá nhà nước")

    @Column(name = "effective_date", nullable = false)
    LocalDate effectiveDate; // Ngày bắt đầu hiệu lực của giá phí

    @Column(name = "end_date", nullable = true)
    LocalDate endDate; // Ngày kết thúc hiệu lực của giá phí (có thể null nếu vẫn còn hiệu lực)
}