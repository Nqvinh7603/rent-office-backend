package com.nqvinh.rentofficebackend.domain.common.entity;

import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE, generator = "notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notifications_seq", allocationSize = 1)
    Long notificationId;

    @Column(name = "consignment_id", nullable = true)
    Long consignmentId;

    @Column(name = "customer_id", nullable = true)
    Long customerId;

    @Column(name = "message", nullable = true, columnDefinition = "TEXT")
    String message;

    @Column(name = "status", nullable = true)
    boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
