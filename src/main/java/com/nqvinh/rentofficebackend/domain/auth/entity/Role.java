package com.nqvinh.rentofficebackend.domain.auth.entity;

import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "roles_seq", allocationSize = 1)
    Long roleId;

    @Column (name = "role_name", unique = true, nullable = false)
    String roleName;

    @Column (name = "description")
    String description;

    @Column (name = "active")
    boolean active;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    List<Permission> permissions;

    @Override
    public String getAuthority() {
        return roleName;
    }
}

