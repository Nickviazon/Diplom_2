package profile;

public class ProfileCredentials {
    public final String email;
    public final String password;
    public final String name;
    private final String accessToken;
    private final String refreshToken;

    public ProfileCredentials(Profile profile, String accessToken, String refreshToken) {
        this.email = profile.email;
        this.password = profile.password;
        this.name = profile.password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ProfileCredentials from (Profile profile, String accessToken, String refreshToken) {
        return new ProfileCredentials(profile, accessToken, refreshToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshTokenToken() {
        return refreshToken;
    }
}
