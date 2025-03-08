package com.wiormiw.simple_perpustakaan_online.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "cover_url", nullable = false)
    private String coverUrl = "https://asset.cloudinary.com/dwshiglkf/47049aada2ea8d5d69a42e06bdc5371d";

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Rent> rents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<RentHistory> rentHistories = new ArrayList<>();

    @Transient // Not persisted
    public int getAvailableQuantity() {
        long activeRents = rents.stream().filter(r -> r.getStatus() == Rent.RentStatus.ACTIVE).count();
        return Math.max(0, quantity - (int) activeRents);
    }
}
