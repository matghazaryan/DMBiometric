package biometric.dm.com.dmbiometricexample;

import java.util.List;

public class User {

    private String email;
    private String password;
    private List<User> userList;

    User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    User(final String email, final String password, final List<User> userList) {
        this.email = email;
        this.password = password;
        this.userList = userList;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<User> getUserList() {
        return userList;
    }
}
