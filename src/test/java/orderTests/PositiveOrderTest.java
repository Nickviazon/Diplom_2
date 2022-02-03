package orderTests;

import clients.AuthClient;
import clients.IngredientClient;
import clients.OrderClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import order.Ingredients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import profile.Profile;
import profile.ProfileBuilder;
import profile.ProfileDirector;
import profile.ProfileType;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class PositiveOrderTest {

    private static OrderClient orderClient;

    @Parameter
    public String accessToken;

    @Parameter(1)
    public int responseCode;

    @Parameters(name="Create order with correct correct ingredients returns {1}")
    public static Object[][] setUpParameters() {
        AuthClient authClient = new AuthClient();
        ProfileDirector profileDirector = new ProfileDirector();
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        orderClient = new OrderClient();

        Profile profile = profileBuilder.getResult();
        String accessToken = authClient.registerProfileResponse(profile)
                .then().assertThat().statusCode(200)
                .extract().path("accessToken");

        return new Object[][] {
                {accessToken, 200},
                {null, 200}
        };
    }

    @Test
    public void createOrderWithCorrectIngredientsReturns200WithNameAndOrderNumber() {
        List<String> ingredientsList = IngredientClient.getIngredientsList();
        Ingredients ingredients = new Ingredients(ingredientsList);

        Response orderCreationResponse = orderClient.createOrderResponse(ingredients, accessToken);
        ValidatableResponse validatableResponse = orderCreationResponse.then().assertThat().statusCode(responseCode);
        assertTrue("Response is unsuccessful", validatableResponse.extract().path("success"));

        String orderName = validatableResponse.extract().path("name");
        assertThat("Order name is wrong", orderName, is(not(blankOrNullString())));

        Integer orderNumber = validatableResponse.extract().path("order.number");
        assertThat("Order number is null", orderNumber, is(notNullValue()));
    }
}
