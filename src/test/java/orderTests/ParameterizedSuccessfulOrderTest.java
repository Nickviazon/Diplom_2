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
import static org.hamcrest.Matchers.blankOrNullString;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class ParameterizedSuccessfulOrderTest {

    private static OrderClient orderClient;

    @Parameter
    public String accessToken;

    @Parameter(1)
    public int responseCode;

    @Parameter(2)
    public boolean isRequestSuccessful;

    @Parameters
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
                {accessToken, 200, true},
                {null, 200, true}
        };
    }

    @Test
    public void createOrderWithCorrectIngredientsReturns200WithNameAndOrderNumber() {
        List<String> ingredientsList = IngredientClient.getIngredientsList();
        Ingredients ingredients = new Ingredients(ingredientsList);

        Response orderCreationResponse = orderClient.createOrderResponse(ingredients, accessToken);
        ValidatableResponse validatableResponse = orderCreationResponse.then().assertThat().statusCode(responseCode);
        validatableResponse.assertThat().body("success", is(true));
        validatableResponse.assertThat().body("name", is(not(blankOrNullString())));
        validatableResponse.assertThat().body("order.number", is(notNullValue()));
    }
}
