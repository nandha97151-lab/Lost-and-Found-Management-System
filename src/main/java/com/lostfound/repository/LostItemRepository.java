package com.lostfound.repository;

import com.lostfound.model.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    List<LostItem> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByStatus(String status);

    @Query("SELECT l FROM LostItem l WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(l.itemName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR l.category = :category) AND " +
           "(:location IS NULL OR :location = '' OR LOWER(l.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:dateLost IS NULL OR l.dateLost = :dateLost)")
    List<LostItem> searchLostItems(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("location") String location,
            @Param("dateLost") LocalDate dateLost
    );
}
