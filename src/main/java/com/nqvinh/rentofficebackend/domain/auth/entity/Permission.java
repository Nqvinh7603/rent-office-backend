package com.nqvinh.rentofficebackend.domain.auth.entity;

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
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_id_seq")
    @SequenceGenerator(name = "permission_id_seq", sequenceName = "permissions_seq", allocationSize = 1)
    Long permissionId;

    @Column(name = "name")
    String name;

    @Column(name = "api_path")
    String apiPath;

    @Column(name = "method")
    String method;

    @Column(name = "module")
    String module;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    List<Role> roles;
}

