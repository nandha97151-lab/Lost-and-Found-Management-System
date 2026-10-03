package com.lostfound.repository;

import com.lostfound.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Claim> findByFoundItemIdOrderByCreatedAtDesc(Long foundItemId);

    long countByStatus(String status);

    boolean existsByFoundItemIdAndUserIdAndStatusIn(Long foundItemId, Long userId, List<String> statuses);
}
