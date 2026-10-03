package com.lostfound.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "found_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", unique = true, length = 50)
    private String itemCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Item name is required")
    @Column(name = "item_name", nullable = false, length = 150)
    private String itemName;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 50)
    private String category;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Location found is required")
    @Column(nullable = false, length = 150)
    private String location;

    @NotNull(message = "Date found is required")
    @Column(name = "date_found", nullable = false)
    private LocalDate dateFound;

    @Column(name = "time_found", length = 20)
    private String timeFound;

    @Column(length = 30)
    private String color;

    @Column(name = "identifying_features", columnDefinition = "TEXT")
    private String identifyingFeatures;

    @Column(name = "found_by_details", columnDefinition = "TEXT")
    private String foundByDetails;

    @Column(length = 255)
    private String image;

    @Column(nullable = false, length = 30)
    private String status; // OPEN, CLAIMED, CLAIM_PENDING

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "OPEN";
        }
        if (itemCode == null || itemCode.isBlank()) {
            itemCode = "FND-" + System.currentTimeMillis() % 1000000;
        }
    }
}
