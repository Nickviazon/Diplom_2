package orderTests;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class ProfileOrderNegativeTest {

    @Test
    @DisplayName("Get Order of unauthorized profile")
    @Description("Get Order of unauthorized profile returns 401 Unauthorized")
    public void getOrderUnauthorizedProfileReturns401WithMessage() {
        String accessToken = null;

        Response getProfileOrdersResponse = new OrderClient().getProfileOrders(accessToken);
        ValidatableResponse validatableResponse = getProfileOrdersResponse.
                then().assertThat().statusCode(401);
        assertFalse("Response must be unsuccessful", validatableResponse.extract().path("success"));

        String expectedMessage = "You should be authorised";
        String actualMessage = validatableResponse.extract().path("message");
        assertThat("Actual message is different from expected", actualMessage, equalTo(expectedMessage));
    }
}
