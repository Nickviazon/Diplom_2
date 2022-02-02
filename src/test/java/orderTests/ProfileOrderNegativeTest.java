package orderTests;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ProfileOrderNegativeTest {

    @Test
    @DisplayName("Get Order of unauthorized profile")
    @Description("Get Order of unauthorized profile returns 401 Unauthorized")
    public void getOrderUnauthorizedProfileReturns401WithMessage() {
        String accessToken = null;

        Response getProfileOrdersResponse = new OrderClient().getProfileOrders(accessToken);
        ValidatableResponse validatableResponse = getProfileOrdersResponse.
                then().assertThat().statusCode(401);
        validatableResponse.assertThat().body("success", is(false));
        validatableResponse.assertThat().body("message", equalTo("You should be authorised"));
    }
}
