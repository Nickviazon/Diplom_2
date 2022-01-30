import clients.AuthClient;
import Profile.Profile;
import Profile.ProfileBuilder;
import Profile.ProfileDirector;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import Profile.ProfileType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class AuthTest {

    private AuthClient authClient;
    private ProfileDirector profileDirector;
    private ProfileBuilder profileBuilder;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        profileDirector = new ProfileDirector();
        profileBuilder = new ProfileBuilder();
    }

    @Test
    public void registerNewProfileReturns200WithAccessAndRefreshTokens() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(200);
        Boolean isResponseSuccessful = validatableResponse.extract().path("success");
        String actualAuthToken = validatableResponse.extract().path("accessToken");
        String actualRefreshToken = validatableResponse.extract().path("refreshToken");

        assertTrue(isResponseSuccessful);
        assertThat("Auth token is null or blank string", actualAuthToken, is(not(blankOrNullString())));
        assertThat("Refresh token is null or blank string", actualRefreshToken, is(not(blankOrNullString())));
    }

}
