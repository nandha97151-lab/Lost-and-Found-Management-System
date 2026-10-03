package com.lostfound.config;

import com.lostfound.model.FoundItem;
import com.lostfound.model.LostItem;
import com.lostfound.model.User;
import com.lostfound.repository.FoundItemRepository;
import com.lostfound.repository.LostItemRepository;
import com.lostfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = userRepository.findByEmail("admin@campus.edu").orElseGet(() -> 
            userRepository.save(User.builder()
                    .name("System Administrator")
                    .email("admin@campus.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("9876543210")
                    .role("ADMIN")
                    .build())
        );

        User user1 = userRepository.findByEmail("alex@campus.edu").orElseGet(() -> 
            userRepository.save(User.builder()
                    .name("Alex Johnson")
                    .email("alex@campus.edu")
                    .password(passwordEncoder.encode("password123"))
                    .phone("9876543211")
                    .role("USER")
                    .build())
        );

        User user2 = userRepository.findByEmail("sophia@campus.edu").orElseGet(() -> 
            userRepository.save(User.builder()
                    .name("Sophia Chen")
                    .email("sophia@campus.edu")
                    .password(passwordEncoder.encode("password123"))
                    .phone("9876543212")
                    .role("USER")
                    .build())
        );

        if (lostItemRepository.count() == 0) {
            lostItemRepository.save(LostItem.builder()
                    .itemCode("LOST-1001")
                    .user(user1)
                    .itemName("MacBook Pro 14\"")
                    .category("Electronics")
                    .description("Space Gray laptop with University CS sticker on top lid.")
                    .location("Central Library - 2nd Floor")
                    .dateLost(LocalDate.now().minusDays(2))
                    .timeLost("14:30")
                    .color("Space Gray")
                    .identifyingFeatures("Apple M2 Pro sticker on lid")
                    .contactInfo("alex@campus.edu")
                    .status("OPEN")
                    .build());

            lostItemRepository.save(LostItem.builder()
                    .itemCode("LOST-1002")
                    .user(user2)
                    .itemName("Leather Wallet")
                    .category("Personal Items")
                    .description("Brown genuine leather wallet containing Student ID.")
                    .location("Student Union Cafeteria")
                    .dateLost(LocalDate.now().minusDays(1))
                    .timeLost("12:15")
                    .color("Brown")
                    .identifyingFeatures("Initials SC engraved on internal flap")
                    .contactInfo("sophia@campus.edu")
                    .status("OPEN")
                    .build());
        }

        if (foundItemRepository.count() == 0) {
            foundItemRepository.save(FoundItem.builder()
                    .itemCode("FND-2001")
                    .user(user2)
                    .itemName("Sony Wireless Headphones")
                    .category("Electronics")
                    .description("Black WH-1000XM4 headphones inside black zip case.")
                    .location("Main Auditorium Row F")
                    .dateFound(LocalDate.now().minusDays(1))
                    .timeFound("17:00")
                    .color("Black")
                    .identifyingFeatures("Red carabiner clip attached to zipper")
                    .foundByDetails("Handed over to Campus Security Desk")
                    .status("OPEN")
                    .build());

            foundItemRepository.save(FoundItem.builder()
                    .itemCode("FND-2002")
                    .user(admin)
                    .itemName("Car & Dorm Key Set")
                    .category("Keys")
                    .description("Set of 3 keys with a metal Batman keychain.")
                    .location("Gymnasium Locker Room")
                    .dateFound(LocalDate.now())
                    .timeFound("11:45")
                    .color("Silver/Black")
                    .identifyingFeatures("Batman emblem metal keychain")
                    .foundByDetails("Stored in Admin Lost & Found Safe")
                    .status("OPEN")
                    .build());
        }
    }
}

