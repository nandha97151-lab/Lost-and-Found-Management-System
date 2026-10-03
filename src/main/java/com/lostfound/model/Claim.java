package com.lostfound.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_code", unique = true, length = 50)
    private String claimCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_item_id", nullable = false)
    private FoundItem foundItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Claimant name is required")
    @Column(name = "claimant_name", nullable = false, length = 100)
    private String claimantName;

    @NotBlank(message = "Ownership proof description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(name = "proof_image", length = 255)
    private String proofImage;

    @Column(nullable = false, length = 30)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
        if (claimCode == null || claimCode.isBlank()) {
            claimCode = "CLM-" + System.currentTimeMillis() % 1000000;
        }
    }
}
