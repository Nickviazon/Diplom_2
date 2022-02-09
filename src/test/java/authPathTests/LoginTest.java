package authPathTests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import profile.Profile;
import profile.ProfileType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginTest extends AuthTest {

    public Profile profile;

    @Before
    @Step("Create profile")
    public void setUp() {
        // Создаём данные для профиля
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        profile = profileBuilder.getResult();
    }

    @Test
    @DisplayName("Login an existed profile")
    @Description("Login an existed profile returns 200 with access and refresh tokens")
    public void loginExistedProfileReturns200WithAccessAndRefreshTokens() {
        // Регистрируем профиль
        authClient.registerProfileResponse(profile).then().assertThat().statusCode(200);

        Response loginResponse = authClient.loginProfileResponse(profile);
        ValidatableResponse validatableResponse = loginResponse.then().assertThat().statusCode(200);
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
    @DisplayName("Login with wrong profile data")
    @Description("Login with wrong profile data return 401 Unauthorized with message in the response body")
    public void loginWithWrongProfileReturns401WithMessage() {
        Response loginResponse = authClient.loginProfileResponse(profile);
        ValidatableResponse validatableResponse = loginResponse.then().assertThat().statusCode(401);
        assertFalse("Response must be unsuccessful", validatableResponse.extract().path("success"));

        String expectedMessage = "email or password are incorrect";
        String actualMessage = validatableResponse.extract().path("message");
        assertThat("Actual message is different from expected", actualMessage, equalTo(expectedMessage));
    }
}
