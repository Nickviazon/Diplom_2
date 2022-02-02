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
public class ParameterizedUpdatePositiveTest extends AuthTest {

    @Parameter
    public ProfileType profileType;

    @Parameter(1)
    public int expectedStatusCode;

    @Parameter(2)
    public boolean expectedSuccessField;


    @Parameters(name="Update authorized profile. Profile type = {0}")
    public static Object[][] setUp() {
        return new Object[][] {
                {FULL, 200, true},
                {WITHOUT_EMAIL, 200, true},
                {WITHOUT_PASSWORD, 200, true},
                {WITHOUT_NAME, 200, true}
        };
    }

    @Test
    public void updateAuthorizedProfileReturns200WithPassedData() {
        // Создаём данные для профиля
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        Profile profile = profileBuilder.getResult();
        ProfileCredentials oldProfileCredentials = ProfileCredentials.from(profile);

        // Создаём новый профиль и получаем accessToken
        ValidatableResponse createProfileResponse = authClient.registerProfileResponse(profile)
                .then().assertThat().statusCode(200);
        String accessToken = createProfileResponse.extract().path("accessToken");

        // Создаём новые данные для пользователя
        profileDirector.buildProfile(profileBuilder, profileType);
        Profile newProfileData = profileBuilder.getResult();
        ProfileCredentials newProfileCredentials = ProfileCredentials.from(newProfileData);

        // Выполняем запрос и проверяем ответ
        Response updateProfileResponse = authClient.updateProfileResponse(newProfileCredentials, accessToken);
        ValidatableResponse validatableResponse = updateProfileResponse.then().assertThat().statusCode(expectedStatusCode);
        validatableResponse.assertThat().body("success", is(expectedSuccessField));

        if (profileType == WITHOUT_EMAIL) {
            validatableResponse.assertThat().body("user.email", equalTo(oldProfileCredentials.email));
       } else {
           validatableResponse.assertThat().body("user.email", equalTo(newProfileCredentials.email));
       }

        if (profileType == WITHOUT_NAME) {
            validatableResponse.assertThat().body("user.name", equalTo(oldProfileCredentials.name));
        } else {
            validatableResponse.assertThat().body("user.name", equalTo(newProfileCredentials.name));
        }
    }




}
