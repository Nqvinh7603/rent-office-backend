package com.nqvinh.rentofficebackend.domain.building.entity;

import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.building.constant.PotentialCustomerStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
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
@Table(name = "customers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customers_seq", allocationSize = 1)
    Long customerId;

    @Column(name = "customer_name", nullable = false)
    String customerName;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "address", nullable = true)
    String address;

    @Column(name = "require_type", nullable = true)
    @Enumerated(EnumType.STRING)
    RequireTypeEnum requireType;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Building> buildings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assign_customer",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> users;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    String note;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    PotentialCustomerStatus status;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Appointment> appointments;
}
