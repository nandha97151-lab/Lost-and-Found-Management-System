package com.lostfound.service;

import com.lostfound.model.Claim;
import com.lostfound.model.FoundItem;
import com.lostfound.model.User;
import com.lostfound.repository.ClaimRepository;
import com.lostfound.repository.FoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Transactional
    public Claim submitClaim(Long foundItemId, User claimant, String claimantName, String description, String additionalInfo, MultipartFile proofImageFile) throws IOException {
        FoundItem foundItem = foundItemRepository.findById(foundItemId)
                .orElseThrow(() -> new IllegalArgumentException("Found item not found."));

        if (!"OPEN".equalsIgnoreCase(foundItem.getStatus())) {
            throw new IllegalStateException("This item is not open for claiming.");
        }

        boolean alreadyClaimed = claimRepository.existsByFoundItemIdAndUserIdAndStatusIn(
                foundItemId, claimant.getId(), List.of("PENDING", "APPROVED"));
        if (alreadyClaimed) {
            throw new IllegalStateException("You already have an active claim on this item.");
        }

        Claim claim = Claim.builder()
                .foundItem(foundItem)
                .user(claimant)
                .claimantName(claimantName)
                .description(description)
                .additionalInfo(additionalInfo)
                .status("PENDING")
                .build();

        if (proofImageFile != null && !proofImageFile.isEmpty()) {
            String imagePath = saveFile(proofImageFile);
            claim.setProofImage(imagePath);
        }

        foundItem.setStatus("CLAIM_PENDING");
        foundItemRepository.save(foundItem);

        return claimRepository.save(claim);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Optional<Claim> getClaimById(Long id) {
        return claimRepository.findById(id);
    }

    public List<Claim> getClaimsByUser(Long userId) {
        return claimRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long countPendingClaims() {
        return claimRepository.countByStatus("PENDING");
    }

    @Transactional
    public void approveClaim(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found"));

        claim.setStatus("APPROVED");
        claimRepository.save(claim);

        FoundItem foundItem = claim.getFoundItem();
        foundItem.setStatus("CLAIMED");
        foundItemRepository.save(foundItem);

        // Reject other pending claims for the same item
        List<Claim> otherClaims = claimRepository.findByFoundItemIdOrderByCreatedAtDesc(foundItem.getId());
        for (Claim other : otherClaims) {
            if (!other.getId().equals(claimId) && "PENDING".equalsIgnoreCase(other.getStatus())) {
                other.setStatus("REJECTED");
                claimRepository.save(other);
            }
        }
    }

    @Transactional
    public void rejectClaim(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found"));

        claim.setStatus("REJECTED");
        claimRepository.save(claim);

        FoundItem foundItem = claim.getFoundItem();
        // Check if there are other pending claims
        List<Claim> pendingClaims = claimRepository.findByFoundItemIdOrderByCreatedAtDesc(foundItem.getId())
                .stream().filter(c -> "PENDING".equalsIgnoreCase(c.getStatus())).toList();

        if (pendingClaims.isEmpty()) {
            foundItem.setStatus("OPEN");
            foundItemRepository.save(foundItem);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName;
    }
}
