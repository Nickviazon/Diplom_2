package profile;

public class Profile {

    public final String email;
    public final String password;
    public final String name;

    public Profile(String email, String password, String name) {
        this.email = email;
        this.password = password ;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Profile(email=%s, name=%s)", email, name);
    }
}
