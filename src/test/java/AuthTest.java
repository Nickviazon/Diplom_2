import clients.AuthClient;
import Profile.Profile;
import Profile.ProfileBuilder;
import Profile.ProfileDirector;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthTest {

    private AuthClient authClient;

    @Before
    public void setUp() {
        authClient = new AuthClient();
    }

    @Test
    public void authRegisterReturns200WithAccessTokenForNewProfile() {

        ProfileDirector profileDirector = new ProfileDirector();
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileDirector.buildFullProfile(profileBuilder);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(200);
        Boolean isResponseSuccessful = validatableResponse.extract().path("success");
        String authToken = validatableResponse.extract().path("accessToken");
        String refreshToken = validatableResponse.extract().path("refreshToken");

        assertTrue(isResponseSuccessful);
        assertNotNull(authToken);
        assertNotNull(refreshToken);
    }
}
