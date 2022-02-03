package authPathTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import profile.Profile;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import profile.ProfileType;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterTest extends AuthTest {

    @Test
    @DisplayName("Register a new profile")
    @Description("Register a new profile returns 200 OK with access and refresh tokens")
    public void registerNewProfileReturns200WithAccessAndRefreshTokens() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(200);
        assertTrue("Response is unsuccessful", validatableResponse.extract().path("success"));

        String accessToken = validatableResponse.extract().path("accessToken");
        assertThat("AccessToken is wrong", accessToken, both(is(not(blankOrNullString()))).and(startsWith("Bearer ")));

        String refreshToken = validatableResponse.extract().path("refreshToken");
        assertThat("RefreshToken is wrong", accessToken, is(not(blankOrNullString())));

        String userEmail = validatableResponse.extract().path("user.email");
        assertThat("Actual user.email is different from expected", userEmail, is(equalTo(profile.email)));

        String userName = validatableResponse.extract().path("user.name");
        assertThat("Actual user.name is different from expected", userName, is(equalTo(profile.name)));
    }

    @Test
    @DisplayName("Register an existed profile")
    @Description("Register an existed profile returns 403 with error the message in the response body")
    public void registerExistedProfileReturns403WithMessage() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile firstProfile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(firstProfile);
        registerResponse.then().assertThat().statusCode(200);

        Profile secondProfile = profileBuilder.getResult();
        registerResponse = authClient.registerProfileResponse(secondProfile);

        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(403);
        assertFalse("Response must be unsuccessful", validatableResponse.extract().path("success"));

        String expectedMessage = "User already exists";
        String actualMessage = validatableResponse.extract().path("message");
        assertThat("Actual message is different from expected", actualMessage, equalTo(expectedMessage));
    }

}
