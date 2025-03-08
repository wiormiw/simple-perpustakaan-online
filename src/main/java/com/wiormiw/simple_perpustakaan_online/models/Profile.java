package com.wiormiw.simple_perpustakaan_online.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl = "https://asset.cloudinary.com/dwshiglkf/0404818aec82bf021ea3d720670fce96";

    @Column(name = "phone_number", nullable = false, length = 13)
    private String phoneNumber;
}
