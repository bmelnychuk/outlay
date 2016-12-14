package com.outlay.domain.model;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public class OutlaySession {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
