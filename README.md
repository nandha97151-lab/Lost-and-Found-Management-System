# Lost & Found Management System

A modern, full-stack **Lost and Found Management System** built with **Java (Spring Boot 3)**, **Spring Data JPA / Hibernate**, **Thymeleaf**, **Spring Security**, and **MySQL**.

---

## 🌟 Key Features

1. **Home Page & Dashboard**:
   - Modern glassmorphism campus hero banner.
   - Dynamic counter statistics (Total Lost, Total Found, Items Returned, Pending Claims).
   - Recently reported lost and found items.

2. **User Authentication & Role-Based Security**:
   - BCrypt password hashing & Spring Security authentication.
   - Student/Staff self-registration and profile management.
   - Roles: `USER` and `ADMIN`.

3. **Report Lost & Found Items**:
   - Unique tracking code auto-generation (`LOST-xxxxxx`, `FND-xxxxxx`).
   - Image upload support stored in local media folder.
   - Detailed metadata fields: category, location, date, time, color, identifying marks, and contact info.

4. **Search & Filter Engine**:
   - Real-time search by keyword, category, location, status, and date.
   - Modern card layout with responsive badges and image fallback.

5. **Ownership Claim System**:
   - Claim submission with detailed proof of ownership description & document/photo attachment.
   - Admin approval/rejection workflow (automatically updates item status to `CLAIMED` and rejects competing claims).

6. **Admin Dashboard**:
   - User account management (view all, remove user).
   - One-click claim approval and rejection.
   - Mark lost items as returned and remove inappropriate reports.

---

## 🛠️ Project Structure

```text
src/main/java/
└── com.lostfound/
    ├── config/            # SecurityConfig, WebMvcConfig, DataInitializer
    ├── controller/        # HomeController, AuthController, LostItemController, etc.
    ├── dto/               # Form transfer objects
    ├── exception/         # GlobalExceptionHandler
    ├── model/             # User, LostItem, FoundItem, Claim Entities
    ├── repository/        # Spring Data JPA Repositories
    └── service/           # Business Logic & File Upload Services
src/main/resources/
    ├── static/css/        # Modern Inter-based CSS (style.css)
    ├── templates/         # Thymeleaf HTML Templates
    └── application.properties
schema.sql                 # MySQL Database DDL & Seed Script
pom.xml                    # Maven Configuration
```

---

## 🚀 Setup & Execution Guide

### Prerequisites
- **Java 17** or higher
- **Maven 3.8+**
- **MySQL Server 8.0+**

### Step 1: Database Setup
1. Start your MySQL Server.
2. Open MySQL Workbench / Command Line and run the script inside `schema.sql`:
   ```sql
   CREATE DATABASE IF NOT EXISTS lost_found_db;
   ```
3. Update `src/main/resources/application.properties` with your MySQL username and password:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

### Step 2: Build & Run Application
Open your terminal in the project directory:

```bash
# Clean and compile
mvn clean package -DskipTests

# Run the Spring Boot Application
mvn spring-boot:run
```

The application will start on **http://localhost:8080**.

---

## 🔑 Default Credentials

- **Admin Account**:
  - Email: `admin@campus.edu`
  - Password: `admin123`
- **Sample User Account**:
  - Email: `alex@campus.edu`
  - Password: `password123`

---

## 💻 Running in IDEs (VS Code / IntelliJ IDEA / Eclipse)

### IntelliJ IDEA:
1. Open IntelliJ IDEA -> `File` -> `Open` -> Select folder `e:\Lost and Found Management System`.
2. Wait for Maven dependencies to sync.
3. Open `src/main/java/com/lostfound/LostFoundApplication.java`.
4. Click the green **Run** button next to `main()`.

### VS Code:
1. Open VS Code -> `File` -> `Open Folder...`.
2. Install **Extension Pack for Java** & **Spring Boot Extension Pack**.
3. Press `F5` or click **Run without Debugging**.

---

## 🔒 Security & Best Practices
- **Passwords**: Hashed with BCrypt (`BCryptPasswordEncoder`).
- **Data Integrity**: Enforced foreign key cascades and Spring Data JPA transactions.
- **Form Input Safety**: Server-side validation via `jakarta.validation` (`@NotBlank`, `@Email`, `@Size`).
- **Media Storage**: Uploads safely mapped via Spring `WebMvcConfigurer`.
