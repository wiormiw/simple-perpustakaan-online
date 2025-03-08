package com.wiormiw.simple_perpustakaan_online.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rents")
public class Rent {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentStatus status = RentStatus.ACTIVE;

    @Column(name = "start_date", updatable = false, nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", updatable = false, nullable = false)
    private LocalDateTime endDate;

    public enum RentStatus {
        ACTIVE,
        OVERDUE,
        COMPLETED,
        COMPLETED_OVERDUE
    }

    @PrePersist
    public void prePersist() {
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusDays(3);
    }
}