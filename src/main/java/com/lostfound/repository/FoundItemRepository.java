package com.lostfound.repository;

import com.lostfound.model.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {

    List<FoundItem> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByStatus(String status);

    @Query("SELECT f FROM FoundItem f WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(f.itemName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR f.category = :category) AND " +
           "(:location IS NULL OR :location = '' OR LOWER(f.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:dateFound IS NULL OR f.dateFound = :dateFound)")
    List<FoundItem> searchFoundItems(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("location") String location,
            @Param("dateFound") LocalDate dateFound
    );
}
