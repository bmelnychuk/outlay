package app.outlay.domain.model;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class Credentials {
    public static Credentials GUEST = new Credentials(null, null);

    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Credentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Credentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isGuestCredentials() {
        return email == null && password == null;
    }
}
