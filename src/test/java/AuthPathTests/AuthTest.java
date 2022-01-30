package AuthPathTests;

import Profile.ProfileBuilder;
import Profile.ProfileDirector;
import clients.AuthClient;
import org.junit.Before;

public abstract class AuthTest {

    protected AuthClient authClient;
    protected ProfileDirector profileDirector;
    protected ProfileBuilder profileBuilder;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        profileDirector = new ProfileDirector();
        profileBuilder = new ProfileBuilder();
    }
}
