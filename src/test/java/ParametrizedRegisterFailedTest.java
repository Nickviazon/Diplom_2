import Profile.*;
import clients.AuthClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static Profile.ProfileType.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class ParametrizedRegisterFailedTest {

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
        AuthClient authClient = new AuthClient();
        ProfileDirector profileDirector = new ProfileDirector();
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileDirector.buildProfile(profileBuilder, profileType);
        Profile profile = profileBuilder.getResult();

        Response registerResponse = authClient.registerProfileResponse(profile);
        ValidatableResponse validatableResponse = registerResponse.then().assertThat().statusCode(expectedResponseCode);
        validatableResponse.body("success", is(false));
        validatableResponse.body("message", is(equalTo(expectedMessage)));
    }
}
