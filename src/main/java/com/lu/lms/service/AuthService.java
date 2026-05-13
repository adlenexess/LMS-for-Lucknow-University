package com.lu.lms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lu.lms.model.Librarian;
import com.lu.lms.model.Role;
import com.lu.lms.model.Student;

public class AuthService {
    private final DatabaseService dbService = DatabaseService.getInstance();

    public boolean registerStudent(Student student) {
        try (Connection conn = dbService.getConnection()) {
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, student.getUsername());
                pstmt.setString(2, student.getPassword()); // Hash in prod
                pstmt.setString(3, Role.STUDENT.name());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    long userId = rs.getLong(1);
                    String studentSql = "INSERT INTO students (user_id, name, email) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(studentSql)) {
                        stmt.setLong(1, userId);
                        stmt.setString(2, student.getName());
                        stmt.setString(3, student.getEmail());
                        stmt.executeUpdate();
                    }
                    student.setId((Long) rs.getLong(1)); // Approx
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerLibrarian(Librarian librarian) {
        try (Connection conn = dbService.getConnection()) {
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, librarian.getUsername());
                pstmt.setString(2, librarian.getPassword());
                pstmt.setString(3, Role.LIBRARIAN.name());
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    long userId = rs.getLong(1);
                    String libSql = "INSERT INTO librarians (user_id, name) VALUES (?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(libSql)) {
                        stmt.setLong(1, userId);
                        stmt.setString(2, librarian.getName());
                        stmt.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Long login(String username, String password, Role role) {
        try (Connection conn = dbService.getConnection()) {
            String sql = "SELECT u.id FROM users u WHERE u.username = ? AND u.password = ? AND u.role = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, role.name());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getStudentId(Long userId) {
        try (Connection conn = dbService.getConnection()) {
            String sql = "SELECT s.id FROM students s JOIN users u ON s.user_id = u.id WHERE u.id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

