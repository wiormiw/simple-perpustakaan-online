package com.wiormiw.simple_perpustakaan_online.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Rent rent;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RentHistory> rentHistory;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Profile profile;

    public boolean isAdmin() {
        return this.roles.stream().anyMatch(role -> role.getName().equals(Role.RoleType.ADMIN));
    }

    public boolean canRent() {
        return this.rent == null || !this.rent.getStatus().equals(Rent.RentStatus.ACTIVE);
    }
}
