package authPathTests;

import profile.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
    public boolean expectedSuccessField;

    @Parameter(3)
    public String expectedMessage;

    @Parameters
    public static Object[][] setUpParameters() {
       return new Object[][] {
               {WITHOUT_EMAIL, 403, false, "Email, password and name are required fields"},
               {WITHOUT_PASSWORD, 403, false, "Email, password and name are required fields"},
               {WITHOUT_NAME, 403, false, "Email, password and name are required fields"}
       };
    }

    @Test
    public void registerProfileWithoutFieldReturns403WithMessage() {
        profileDirector.buildProfile(profileBuilder, profileType);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(expectedResponseCode);
        validatableResponse.assertThat().body("success", is(false));
        validatableResponse.assertThat().body("message", is(equalTo(expectedMessage)));
    }
}
