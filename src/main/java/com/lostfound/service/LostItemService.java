package com.lostfound.service;

import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.repository.LostItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LostItemService {

    @Autowired
    private LostItemRepository lostItemRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public LostItem saveLostItem(LostItem lostItem, User user, MultipartFile file) throws IOException {
        lostItem.setUser(user);
        if (file != null && !file.isEmpty()) {
            String imagePath = saveFile(file);
            lostItem.setImage(imagePath);
        }
        return lostItemRepository.save(lostItem);
    }

    public List<LostItem> getAllLostItems() {
        return lostItemRepository.findAll();
    }

    public Optional<LostItem> getLostItemById(Long id) {
        return lostItemRepository.findById(id);
    }

    public List<LostItem> getLostItemsByUser(Long userId) {
        return lostItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<LostItem> searchLostItems(String keyword, String category, String location, LocalDate dateLost) {
        return lostItemRepository.searchLostItems(keyword, category, location, dateLost);
    }

    public long countTotalLost() {
        return lostItemRepository.count();
    }

    public long countReturned() {
        return lostItemRepository.countByStatus("RETURNED");
    }

    public void updateStatus(Long id, String status) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lost item not found"));
        item.setStatus(status);
        lostItemRepository.save(item);
    }

    public void deleteLostItem(Long id) {
        lostItemRepository.deleteById(id);
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
