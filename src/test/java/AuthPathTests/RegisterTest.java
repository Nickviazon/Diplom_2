package AuthPathTests;

import Profile.Profile;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import Profile.ProfileType;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

public class RegisterTest extends AuthTest {

    @Test
    public void registerNewProfileReturns200WithAccessAndRefreshTokens() {
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(200);
        validatableResponse.body("success", is(true));
        validatableResponse.body("accessToken", is(not(blankOrNullString())));
        validatableResponse.body("refreshToken", is(not(blankOrNullString())));
        validatableResponse.body("user.email", is(equalTo(profile.email.toLowerCase())));
        validatableResponse.body("user.name", is(equalTo(profile.name)));
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
        validatableResponse.body("success", is(false));
        validatableResponse.body("message", is(equalTo(expectedMessage)));
    }

}