package profile;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileCredentials {
    public final String email;
    public final String password;
    public final String name;

    public ProfileCredentials(Profile profile) {
        this.email = profile.email;
        this.password = profile.password;
        this.name = profile.name;
    }

    public static ProfileCredentials from(Profile profile) {
        return new ProfileCredentials(profile);
    }

    public Profile toProfile() {
        return new Profile(email, password, name);
    }
}
