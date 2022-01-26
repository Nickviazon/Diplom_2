package clients.Profile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Profile {

    public final String email;
    public final String password;
    public final String name;

    public Profile(String email, String password, String name) {
        this.email = email;
        this.password = password ;
        this.name = name;
    }
}
