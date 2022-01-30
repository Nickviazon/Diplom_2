package AuthPathTests;

import Profile.Profile;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import Profile.ProfileType;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

public class RegisterTest extends AuthTest {

    @Test
    public void registerNewProfileReturns200WithAccessAndRefreshTokens() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(200);
        validatableResponse.assertThat().body("success", is(true));
        validatableResponse.assertThat().body("accessToken", both(is(not(blankOrNullString()))).and(startsWith("Bearer ")));
        validatableResponse.assertThat().body("refreshToken", is(not(blankOrNullString())));
        validatableResponse.assertThat().body("user.email", is(equalTo(profile.email.toLowerCase())));
        validatableResponse.assertThat().body("user.name", is(equalTo(profile.name)));
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
        validatableResponse.assertThat().body("success", is(false));
        validatableResponse.assertThat().body("message", is(equalTo(expectedMessage)));
    }

}
