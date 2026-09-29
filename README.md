# Quản Lý Kho Văn Phòng Phẩm (Office Supplies Inventory Management)

## Project Structure

```
.
├── frontend/          # Java Swing/JFrame Desktop Application
│   ├── src/main/java/com/quanlykhov/ui/
│   ├── src/main/resources/
│   └── pom.xml
├── backend/           # Java Backend Service
│   ├── src/main/java/com/quanlykhov/api/
│   ├── src/main/java/com/quanlykhov/dao/
│   ├── src/main/resources/
│   └── pom.xml
├── database/          # Database Scripts
│   └── sql/
├── pom.xml            # Parent Maven Configuration
└── README.md
```

## Setup Instructions

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL/SQL Server

### Database Setup
1. Navigate to `database/sql/`
2. Run the initialization scripts

### Run Frontend
```bash
cd frontend
mvn clean install
mvn exec:java
```

### Run Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## Team Members
- Add team member names here

## Features
- Inventory management
- Product tracking
- Stock reports
