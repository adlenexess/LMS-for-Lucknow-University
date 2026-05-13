package com.lu.lms.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lu.lms.model.Issue;

public class IssueService {
    private final DatabaseService dbService = DatabaseService.getInstance();
    private final BookService bookService = new BookService();

    public void issueBook(Long studentId, Long bookId) {
        Issue issue = new Issue(studentId, bookId);
        try (Connection conn = dbService.getConnection()) {
            // Check book available
            String checkSql = "SELECT available FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setLong(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getBoolean("available")) {
                // Insert issue
                String issueSql = "INSERT INTO issues (student_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(issueSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setLong(1, studentId);
                    pstmt.setLong(2, bookId);
                    pstmt.setDate(3, Date.valueOf(issue.getIssueDate()));
                    pstmt.setDate(4, Date.valueOf(issue.getDueDate()));
                    pstmt.executeUpdate();
                }
                // Update book
                bookService.setBookAvailable(bookId, false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(Long issueId) {
        try (Connection conn = dbService.getConnection()) {
            String sql = "UPDATE issues SET return_date = ?, fine = ? WHERE id = ?";
            // First get due_date to calc fine
            String dueSql = "SELECT due_date FROM issues WHERE id = ?";
            PreparedStatement dueStmt = conn.prepareStatement(dueSql);
            dueStmt.setLong(1, issueId);
            ResultSet rs = dueStmt.executeQuery();
            double fine = 0;
            LocalDate dueDate = null;
            if (rs.next()) {
                dueDate = rs.getDate("due_date").toLocalDate();
            }
            LocalDate returnDate = LocalDate.now();
            if (dueDate != null && returnDate.isAfter(dueDate)) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
                fine = days * 50;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, Date.valueOf(returnDate));
                pstmt.setDouble(2, fine);
                pstmt.setLong(3, issueId);
                pstmt.executeUpdate();
            }
            // Get book_id to make available
            String bookSql = "SELECT book_id FROM issues WHERE id = ?";
            PreparedStatement bookStmt = conn.prepareStatement(bookSql);
            bookStmt.setLong(1, issueId);
            ResultSet bookRs = bookStmt.executeQuery();
            if (bookRs.next()) {
                bookService.setBookAvailable(bookRs.getLong("book_id"), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Issue> getIssuesByStudent(Long studentId) {
        List<Issue> issues = new ArrayList<>();
        try (Connection conn = dbService.getConnection()) {
            String sql = "SELECT * FROM issues WHERE student_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, studentId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Issue issue = new Issue();
                    issue.setId(rs.getLong("id"));
                    issue.setStudentId(rs.getLong("student_id"));
                    issue.setBookId(rs.getLong("book_id"));
                    issue.setIssueDate(rs.getDate("issue_date").toLocalDate());
                    issue.setDueDate(rs.getDate("due_date").toLocalDate());
                    if (rs.getDate("return_date") != null) {
                        issue.setReturnDate(rs.getDate("return_date").toLocalDate());
                    }
                    issue.setFine(rs.getDouble("fine"));
                    issues.add(issue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }
}
