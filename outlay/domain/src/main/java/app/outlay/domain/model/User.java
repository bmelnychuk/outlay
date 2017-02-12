package app.outlay.domain.model;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class User {
    private String id;
    private String email;
    private String token;
    private boolean anonymous;
    private String userName;

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

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public User setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
