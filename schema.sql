# Lost & Found Management System Database Schema & Sample Data

CREATE DATABASE IF NOT EXISTS lost_found_db;
USE lost_found_db;

-- 1. Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Lost Items table
CREATE TABLE IF NOT EXISTS lost_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_code VARCHAR(50) UNIQUE,
    user_id BIGINT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(150) NOT NULL,
    date_lost DATE NOT NULL,
    time_lost VARCHAR(20),
    color VARCHAR(30),
    identifying_features TEXT,
    contact_info VARCHAR(150),
    image VARCHAR(255),
    status VARCHAR(30) DEFAULT 'OPEN',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Found Items table
CREATE TABLE IF NOT EXISTS found_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_code VARCHAR(50) UNIQUE,
    user_id BIGINT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(150) NOT NULL,
    date_found DATE NOT NULL,
    time_found VARCHAR(20),
    color VARCHAR(30),
    identifying_features TEXT,
    found_by_details TEXT,
    image VARCHAR(255),
    status VARCHAR(30) DEFAULT 'OPEN',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Claims table
CREATE TABLE IF NOT EXISTS claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_code VARCHAR(50) UNIQUE,
    found_item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    claimant_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    additional_info TEXT,
    proof_image VARCHAR(255),
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (found_item_id) REFERENCES found_items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- SAMPLE SEED DATA
-- Passwords are BCrypt hashed for "password123": $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnn5.iK3W/t1.0bS
INSERT IGNORE INTO users (id, name, email, password, phone, role, created_at) VALUES
(1, 'System Administrator', 'admin@campus.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnn5.iK3W/t1.0bS', '9876543210', 'ADMIN', NOW()),
(2, 'Alex Johnson', 'alex@campus.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnn5.iK3W/t1.0bS', '9876543211', 'USER', NOW()),
(3, 'Sophia Chen', 'sophia@campus.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnn5.iK3W/t1.0bS', '9876543212', 'USER', NOW());

INSERT IGNORE INTO lost_items (id, item_code, user_id, item_name, category, description, location, date_lost, time_lost, color, identifying_features, contact_info, image, status, created_at) VALUES
(1, 'LOST-1001', 2, 'MacBook Pro 14"', 'Electronics', 'Space Gray laptop with a University CS stickers on lid.', 'Central Library - 2nd Floor', '2026-09-28', '14:30', 'Space Gray', 'Apple M2 Pro sticker, slight dent on top left corner', 'alex@campus.edu / 9876543211', '/uploads/macbook_sample.png', 'OPEN', NOW()),
(2, 'LOST-1002', 3, 'Leather Wallet', 'Personal Items', 'Brown genuine leather wallet containing Student ID and driver license.', 'Student Union Cafeteria', '2026-09-30', '12:15', 'Brown', 'Initials SC engraved on internal flap', 'sophia@campus.edu', '/uploads/wallet_sample.png', 'OPEN', NOW()),
(3, 'LOST-1003', 2, 'Calculus III Notebook', 'Books & Notes', 'Spiral bound blue hardcover notebook with handwritten notes.', 'Engineering Block B Room 302', '2026-10-01', '09:00', 'Blue', 'Name written on front cover page', 'alex@campus.edu', NULL, 'RETURNED', NOW());

INSERT IGNORE INTO found_items (id, item_code, user_id, item_name, category, description, location, date_found, time_found, color, identifying_features, found_by_details, image, status, created_at) VALUES
(1, 'FND-2001', 3, 'Sony Noise-Canceling Headphones', 'Electronics', 'Black WH-1000XM4 headphones inside black zip case.', 'Main Auditorium Row F', '2026-09-29', '17:00', 'Black', 'Custom red carabiner clip attached to zipper', 'Handed over to Campus Security Desk', '/uploads/headphones_sample.png', 'OPEN', NOW()),
(2, 'FND-2002', 1, 'Car & Dorm Key Set', 'Keys', 'Set of 3 keys with a metal Batman keychain.', 'Gymnasium Locker Room', '2026-10-02', '11:45', 'Silver/Black', 'Batman emblem metal keychain', 'Stored in Admin Lost & Found Safe', '/uploads/keys_sample.png', 'OPEN', NOW());

INSERT IGNORE INTO claims (id, claim_code, found_item_id, user_id, claimant_name, description, additional_info, proof_image, status, created_at) VALUES
(1, 'CLM-3001', 1, 2, 'Alex Johnson', 'I dropped my Sony WH-1000XM4 headphones in Auditorium after the guest lecture. It has a red carabiner clip on the case.', 'Serial number ending in 8849', NULL, 'PENDING', NOW());
