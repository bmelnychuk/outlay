package com.outlay.domain.model;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class User {
    private String id;
    private String email;

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
}
