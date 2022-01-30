package AuthPathTests;

import Profile.ProfileBuilder;
import Profile.ProfileDirector;
import clients.AuthClient;
import org.junit.BeforeClass;

public abstract class AuthTest {

    protected static AuthClient authClient;
    protected static ProfileDirector profileDirector;
    protected static ProfileBuilder profileBuilder;

    @BeforeClass
    public static void setUpClientAndBuilders() {
        authClient = new AuthClient();
        profileDirector = new ProfileDirector();
        profileBuilder = new ProfileBuilder();
    }
}
