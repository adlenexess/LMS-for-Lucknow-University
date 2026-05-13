package com.lu.lms.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlertService {
    private final DatabaseService dbService = DatabaseService.getInstance();

    public List<String> getAlerts() {
        List<String> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try (Connection conn = dbService.getConnection()) {
            String sql = "SELECT i.id, i.due_date, s.name as student_name, b.title as book_title " +
                         "FROM issues i " +
                         "JOIN students s ON i.student_id = s.id " +
                         "JOIN books b ON i.book_id = b.id " +
                         "WHERE i.return_date IS NULL AND i.due_date <= ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(today.plusDays(5)));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String alert = "Alert: Student " + rs.getString("student_name") +
                                   " has book '" + rs.getString("book_title") + "' due on " + rs.getDate("due_date");
                    alerts.add(alert);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alerts;
    }
}
