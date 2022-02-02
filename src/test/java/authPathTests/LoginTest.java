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
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

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
        validatableResponse.assertThat().body("success", is(true));
        validatableResponse.assertThat().body("accessToken", both(is(not(blankOrNullString()))).and(startsWith("Bearer ")));
        validatableResponse.assertThat().body("refreshToken", is(not(blankOrNullString())));
        validatableResponse.assertThat().body("user.email", is(equalTo(profile.email.toLowerCase())));
        validatableResponse.assertThat().body("user.name", is(equalTo(profile.name)));
    }

    @Test
    @DisplayName("Login with wrong profile data")
    @Description("Login with wrong profile data return 401 Unauthorized with message in the response body")
    public void loginWithWrongProfileReturns401WithMessage() {
        String expectedMessage = "email or password are incorrect";

        Response loginResponse = authClient.loginProfileResponse(profile);
        ValidatableResponse validatableResponse = loginResponse.then().assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", is(false));
        validatableResponse.assertThat().body("message", equalTo(expectedMessage));
    }
}
