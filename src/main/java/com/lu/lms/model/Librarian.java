package com.lu.lms.model;

public class Librarian extends User {
    private String name;

    public Librarian() {
        super();
        setRole(Role.LIBRARIAN);
    }

    public Librarian(String username, String password, String name) {
        super(username, password, Role.LIBRARIAN);
        this.name = name;
    }

    // Getters/Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
