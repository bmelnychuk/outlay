package com.outlay.domain.model;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class User {
    public static final User ANONYMOUS = new User();

    static {
        ANONYMOUS.setAnonymous(true);
    }

    private String id;
    private String email;
    private String token;
    private boolean anonymous;

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Deprecated
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
