package com.lu.lms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lu.lms.model.Book;

public class BookService {
    private final DatabaseService dbService = DatabaseService.getInstance();

    public void addBook(Book book) {
        try (Connection conn = dbService.getConnection()) {
            String sql = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.setString(3, book.getIsbn());
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = dbService.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setAvailable(rs.getBoolean("available"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void setBookAvailable(Long bookId, boolean available) {
        try (Connection conn = dbService.getConnection()) {
            String sql = "UPDATE books SET available = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, available);
                pstmt.setLong(2, bookId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
