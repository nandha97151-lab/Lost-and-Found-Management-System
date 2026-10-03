package com.lostfound.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lost_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostItem {

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

    @NotBlank(message = "Location lost is required")
    @Column(nullable = false, length = 150)
    private String location;

    @NotNull(message = "Date lost is required")
    @Column(name = "date_lost", nullable = false)
    private LocalDate dateLost;

    @Column(name = "time_lost", length = 20)
    private String timeLost;

    @Column(length = 30)
    private String color;

    @Column(name = "identifying_features", columnDefinition = "TEXT")
    private String identifyingFeatures;

    @NotBlank(message = "Contact information is required")
    @Column(name = "contact_info", nullable = false, length = 150)
    private String contactInfo;

    @Column(length = 255)
    private String image;

    @Column(nullable = false, length = 30)
    private String status; // OPEN, CLAIMED, RETURNED

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
            itemCode = "LOST-" + System.currentTimeMillis() % 1000000;
        }
    }
}
