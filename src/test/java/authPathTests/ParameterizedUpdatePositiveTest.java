package authPathTests;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import profile.Profile;
import profile.ProfileCredentials;
import profile.ProfileType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static profile.ProfileType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class ParameterizedUpdatePositiveTest extends AuthTest {

    @Parameter
    public ProfileType profileType;

    @Parameter(1)
    public int expectedStatusCode;


    @Parameters(name="Update authorized profile. Profile type = {0}")
    public static Object[][] setUp() {
        return new Object[][] {
                {FULL, 200},
                {WITHOUT_EMAIL, 200},
                {WITHOUT_PASSWORD, 200},
                {WITHOUT_NAME, 200}
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
        assertTrue("Response is unsuccessful", validatableResponse.extract().path("success"));

        String expectedUserEmail = profileType == WITHOUT_EMAIL ? oldProfileCredentials.email : newProfileCredentials.email;
        String actualUserEmail = validatableResponse.extract().path("user.email");
        assertThat("Updated user.email is different from expected", actualUserEmail, equalTo(expectedUserEmail));


        String expectedUserName = profileType == WITHOUT_NAME ? oldProfileCredentials.name : newProfileCredentials.name;
        String actualUserName = validatableResponse.extract().path("user.name");
        assertThat("Actual user.name is different from expected", actualUserName, is(equalTo(expectedUserName)));
    }




}
