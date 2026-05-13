package com.lu.lms.model;

public class UserSession {
    private static Long currentUserId;
    private static Role currentRole;

    public static void setCurrentUserId(Long id) {
        currentUserId = id;
    }

    public static Long getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentRole(Role role) {
        currentRole = role;
    }

    public static Role getCurrentRole() {
        return currentRole;
    }

    public static void logout() {
        currentUserId = null;
        currentRole = null;
    }

    public static boolean isLoggedIn() {
        return currentUserId != null;
    }
}

