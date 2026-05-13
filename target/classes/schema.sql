-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS librarians (
    id IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS books (
    id IDENTITY PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS issues (
    id IDENTITY PRIMARY KEY,
    student_id BIGINT REFERENCES students(id),
    book_id BIGINT REFERENCES books(id),
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    fine DOUBLE DEFAULT 0
);

-- Sample data
INSERT INTO users (username, password, role) VALUES ('libadmin', 'admin123', 'LIBRARIAN');
INSERT INTO librarians (user_id, name) VALUES (1, 'Admin Librarian');

INSERT INTO users (username, password, role) VALUES ('student1', 'pass123', 'STUDENT');
INSERT INTO students (user_id, name, email) VALUES (2, 'John Doe', 'john@lu.ac.in');

INSERT INTO books (title, author, isbn, available) VALUES 
('JavaFX Guide', 'Author1', '1234567890', true),
('Database Systems', 'Author2', '0987654321', true),
('Algorithms', 'Author3', '1122334455', true),
('Data Structures', 'Author4', '1111222233', true),
('Computer Networks', 'Author5', '4444555566', true);
