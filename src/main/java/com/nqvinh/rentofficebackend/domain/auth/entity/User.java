package com.nqvinh.rentofficebackend.domain.auth.entity;

import com.nqvinh.rentofficebackend.domain.auth.enums.GenderEnum;
import com.nqvinh.rentofficebackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "username", unique = true)
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "active")
    boolean active;

    String avatar;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
