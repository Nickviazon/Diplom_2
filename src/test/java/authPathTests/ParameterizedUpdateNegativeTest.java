package authPathTests;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import profile.Profile;
import profile.ProfileCredentials;
import profile.ProfileType;

import static profile.ProfileType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class ParameterizedUpdateNegativeTest extends AuthTest {

    @Parameter
    public ProfileType profileType;

    @Parameter(1)
    public int expectedStatusCode;

    @Parameter(2)
    public boolean expectedSuccessField;

    @Parameter(3)
    public String expectedMessage;


    @Parameters
    public static Object[][] setUp() {
        return new Object[][]{
                {FULL, 401, false, "You should be authorised"},
                {WITHOUT_EMAIL, 401, false, "You should be authorised"},
                {WITHOUT_PASSWORD, 401, false, "You should be authorised"},
                {WITHOUT_NAME, 401, false, "You should be authorised"}
        };
    }

    @Test
    public void updateProfileWithoutAuthorizeReturns401WithMessage() {
        // Создаём данные для профиля
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();
        ProfileCredentials oldProfileCredentials = ProfileCredentials.from(profile);

        // Создаём новый профиль
        ValidatableResponse createProfileResponse = authClient.registerProfileResponse(profile)
                .then().assertThat().statusCode(200);

        // Создаём новые данные для пользователя
        profileDirector.buildProfile(profileBuilder, profileType);
        Profile newProfileData = profileBuilder.getResult();
        ProfileCredentials newProfileCredentials = ProfileCredentials.from(newProfileData);
        // Задаём пустой accessToken
        String accessToken = null;

        Response updateProfileResponse = authClient.updateProfileResponse(newProfileCredentials, accessToken);
        ValidatableResponse validatableResponse = updateProfileResponse.then().assertThat().statusCode(401);
        validatableResponse.assertThat().body("message", equalTo(expectedMessage));
    }
}

