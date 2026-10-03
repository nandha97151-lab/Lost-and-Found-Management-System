package com.lostfound.service;

import com.lostfound.model.FoundItem;
import com.lostfound.model.User;
import com.lostfound.repository.FoundItemRepository;
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
public class FoundItemService {

    @Autowired
    private FoundItemRepository foundItemRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public FoundItem saveFoundItem(FoundItem foundItem, User user, MultipartFile file) throws IOException {
        foundItem.setUser(user);
        if (file != null && !file.isEmpty()) {
            String imagePath = saveFile(file);
            foundItem.setImage(imagePath);
        }
        return foundItemRepository.save(foundItem);
    }

    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    public Optional<FoundItem> getFoundItemById(Long id) {
        return foundItemRepository.findById(id);
    }

    public List<FoundItem> getFoundItemsByUser(Long userId) {
        return foundItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<FoundItem> searchFoundItems(String keyword, String category, String location, LocalDate dateFound) {
        return foundItemRepository.searchFoundItems(keyword, category, location, dateFound);
    }

    public long countTotalFound() {
        return foundItemRepository.count();
    }

    public void updateStatus(Long id, String status) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Found item not found"));
        item.setStatus(status);
        foundItemRepository.save(item);
    }

    public void deleteFoundItem(Long id) {
        foundItemRepository.deleteById(id);
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
