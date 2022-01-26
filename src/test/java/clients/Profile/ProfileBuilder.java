package clients.Profile;

public class ProfileBuilder implements Builder {
    private String email;
    private String password;
    private String name;

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Profile getResult() {
        return new Profile(email, password, name);
    }
}
