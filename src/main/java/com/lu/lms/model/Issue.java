package com.lu.lms.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Issue {
    private Long id;
    private Long studentId;
    private Long bookId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine = 0.0;

    public Issue() {}

    public Issue(Long studentId, Long bookId) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(5);
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        calculateFine();
    }

    private void calculateFine() {
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            this.fine = daysOverdue * 50.0;
        }
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public boolean isNearExpiry() {
        return LocalDate.now().until(dueDate).getDays() <= 5;
    }
}
