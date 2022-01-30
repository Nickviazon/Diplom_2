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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

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
        boolean isResponseSuccessful = validatableResponse.extract().path("success");
        String actualAuthToken = validatableResponse.extract().path("accessToken");
        String actualRefreshToken = validatableResponse.extract().path("refreshToken");

        assertTrue(isResponseSuccessful);
        assertThat("Auth token is null or blank string", actualAuthToken, is(not(blankOrNullString())));
        assertThat("Refresh token is null or blank string", actualRefreshToken, is(not(blankOrNullString())));
    }

    @Test
    public void registerExistedProfileReturns403WithMessage() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile firstProfile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(firstProfile);
        registerResponse.then().assertThat().statusCode(200);

        String expectedMessage = "User already exists";
        Profile secondProfile = profileBuilder.getResult();
        registerResponse = authClient.registerProfileResponse(secondProfile);

        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(403);
        boolean isResponseSuccessful = validatableResponse.extract().path("success");
        String actualMessage = validatableResponse.extract().path("message");

        assertFalse(isResponseSuccessful);
        assertEquals(expectedMessage, actualMessage);
    }

}
