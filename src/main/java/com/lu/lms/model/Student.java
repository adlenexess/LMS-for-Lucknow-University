package com.lu.lms.model;

public class Student extends User {
    private String name;
    private String email;

    public Student() {
        super();
        setRole(Role.STUDENT);
    }

    public Student(String username, String password, String name, String email) {
        super(username, password, Role.STUDENT);
        this.name = name;
        this.email = email;
    }

    // Getters/Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
