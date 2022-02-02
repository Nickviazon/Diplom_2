package orderTests;

import clients.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import order.Ingredients;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class OrderNegativeTest {

    private static OrderClient orderClient;

    @BeforeClass
    public static void setUpParameters() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Create order with empty list")
    @Description("Create order with empty list of ingredients returns 400 with message")
    public void createOrderWithEmptyIngredientsListReturns400WithMessage() {
        Ingredients ingredients = new Ingredients(emptyList());
        String accessToken = null;

        Response orderCreationResponse = orderClient.createOrderResponse(ingredients, accessToken);
        ValidatableResponse validatableResponse = orderCreationResponse.then().assertThat().statusCode(400);
        validatableResponse.assertThat().body("success", is(false));
        validatableResponse.assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Crete order with incorrect ingredients hash")
    @Description("Crete order with incorrect ingredients hash returns 500 Internal Server Error")
    public void createOrderWithIncorrectIngredientsHashReturns500InternalServerError() {
        List<String> randomIngredientsHash = Ingredients.getRandomIngredients();
        Ingredients ingredients = new Ingredients(randomIngredientsHash);
        String accessToken = null;

        Response orderCreationResponse = orderClient.createOrderResponse(ingredients, accessToken);
        ValidatableResponse validatableResponse = orderCreationResponse.then().assertThat().statusCode(500);
    }
}
