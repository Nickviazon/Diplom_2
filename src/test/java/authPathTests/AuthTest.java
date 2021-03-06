package authPathTests;

import io.qameta.allure.Step;
import profile.ProfileBuilder;
import profile.ProfileDirector;
import clients.AuthClient;
import org.junit.BeforeClass;

public abstract class AuthTest {

    protected static AuthClient authClient;
    protected static ProfileDirector profileDirector;
    protected static ProfileBuilder profileBuilder;

    @BeforeClass
    @Step("Create clients")
    public static void setUpClientAndBuilders() {
        authClient = new AuthClient();
        profileDirector = new ProfileDirector();
        profileBuilder = new ProfileBuilder();
    }
}
