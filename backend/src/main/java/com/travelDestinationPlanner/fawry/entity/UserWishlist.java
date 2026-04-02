package com.travelDestinationPlanner.fawry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(
        name = "user_wishlist",
        uniqueConstraints = @UniqueConstraint(name = "unique_user_destination", columnNames = {"user_id", "destination_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;
}
