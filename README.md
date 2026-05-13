that # Lucknow University LMS (JavaFX + H2)

A desktop Library Management System (LMS) built with **JavaFX** and **H2 (embedded)** database.

## Features

- **Student workflow**
  - Login / Logout
  - View available books
  - Issue a book
  - Return a book (with fine calculation)
  - View your active/returned issues
- **Librarian workflow**
  - Login / Logout
  - View alerts (prototype)
  - View issues for a selected student (prototype)
  - “Send alerts” (prototype UI action)
- Uses an embedded H2 database with schema initialization from `src/main/resources/schema.sql`.

## Project Structure

- `src/main/java/com/lu/lms/MainApp.java` – JavaFX entry point + scene switching
- `src/main/java/com/lu/lms/controller/*Controller.java` – JavaFX controllers
- `src/main/java/com/lu/lms/model/*` – domain models
- `src/main/java/com/lu/lms/service/*Service.java` – business logic / DB operations
- `src/main/resources/view/*.fxml` – UI views
- `src/main/resources/schema.sql` – H2 schema + sample data

## Tech Stack

- **Java 25** (per `pom.xml`)
- **JavaFX 21**
- **Maven**
- **H2 Database 2.x** (embedded)

## Getting Started

### Prerequisites

- JDK compatible with the project (JavaFX requires JDK 21 runtime; `pom.xml` uses source/target 25)
- Maven

### Build

```bash
mvn clean package
```

### Run

```bash
mvn javafx:run
```

## Database

The database is embedded using:

- JDBC URL: `jdbc:h2:./lmsdb`
- Schema + sample data: `classpath:schema.sql`

When the app runs, it initializes the schema (if not already present) in the project directory.

## Demo / Test Accounts (from `schema.sql`)

- **Librarian**
  - Username: `libadmin`
  - Password: `admin123`
  - Role: `LIBRARIAN`

- **Student**
  - Username: `student1`
  - Password: `pass123`
  - Role: `STUDENT`

## Notes

- Some librarian parts are marked as prototype/hardcoded (e.g., student selection).
- Passwords are stored as plain text in this prototype (no hashing).

## College Assignment

This repository is intended for a **college assignment / academic project** demonstrating a simple LMS using JavaFX and an embedded database.

## Design Patterns Used (based on the current implementation)

- **MVC (Model–View–Controller)**
  - Models: `com.lu.lms.model.*`
  - Views: `src/main/resources/view/*.fxml`
  - Controllers: `com.lu.lms.controller.*`
- **Singleton**
  - `DatabaseService` uses `getInstance()` to ensure a single DB service instance.
- **Facade / Service Layer (Separation of Concerns)**
  - Controllers delegate business + DB operations to `*Service` classes (`AuthService`, `BookService`, `IssueService`, `AlertService`).
- **Factory Method (Practical form via Factory-like construction)**
  - JavaFX view loading is centralized through `FXMLLoader` in `MainApp` to create/show scenes from FXML paths.


