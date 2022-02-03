package authPathTests;

import profile.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static profile.ProfileType.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class ParametrizedRegisterNegativeTest extends AuthTest {

    @Parameter
    public ProfileType profileType;

    @Parameter(1)
    public int expectedResponseCode;

    @Parameter(2)
    public String expectedMessage;

    @Parameters(name="Register profile {0} returns {1} with message in the response body")
    public static Object[][] setUpParameters() {
       return new Object[][] {
               {WITHOUT_EMAIL, 403, "Email, password and name are required fields"},
               {WITHOUT_PASSWORD, 403, "Email, password and name are required fields"},
               {WITHOUT_NAME, 403, "Email, password and name are required fields"}
       };
    }

    @Test
    public void registerProfileWithoutFieldReturns403WithMessage() {
        profileDirector.buildProfile(profileBuilder, profileType);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(expectedResponseCode);
        assertFalse("Response must be unsuccessful", validatableResponse.extract().path("success"));

        String actualMessage = validatableResponse.extract().path("message");
        assertThat("Actual message is different from expected", actualMessage, equalTo(expectedMessage));
    }
}
